/*
 * Copyright 2018 EPAM Systems
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.refleqt.general.support.reportportal.agent;

import com.epam.reportportal.listeners.ListenerParameters;
import com.epam.reportportal.service.Launch;
import com.epam.reportportal.service.ReportPortal;
import com.epam.reportportal.utils.properties.PropertiesLoader;
import com.epam.ta.reportportal.ws.model.FinishExecutionRQ;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import com.epam.ta.reportportal.ws.model.launch.StartLaunchRQ;
import com.epam.ta.reportportal.ws.model.log.SaveLogRQ;
import cucumber.api.*;
import cucumber.api.event.*;
import gherkin.deps.com.google.gson.Gson;
import gherkin.deps.com.google.gson.JsonObject;
import io.cucumber.datatable.dependency.com.fasterxml.jackson.databind.JsonNode;
import io.cucumber.datatable.dependency.com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Maybe;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import java.io.IOException;
import java.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import rp.com.google.common.base.Supplier;
import rp.com.google.common.base.Suppliers;

/**
 * Abstract Cucumber 4.x formatter for Report Portal
 *
 * @author Sergey Gvozdyukevich
 * @author Andrei Varabyeu
 * @author Serhii Zharskyi
 * @author Vitaliy Tsvihun
 */
public abstract class AbstractReporter implements ConcurrentEventListener {

    Supplier<Launch> launch;
    static final String COLON_INFIX = ": ";

    private Map<String, RunningContext.FeatureContext> currentFeatureContextMap =
            Collections.synchronizedMap(new HashMap<String, RunningContext.FeatureContext>());

    private Map<ImmutablePair<String, String>, RunningContext.ScenarioContext> currentScenarioContextMap =
            Collections.synchronizedMap(new HashMap<ImmutablePair<String, String>, RunningContext.ScenarioContext>());

    private Map<Long, RunningContext.ScenarioContext> threadCurrentScenarioContextMap =
            Collections.synchronizedMap(new HashMap<Long, RunningContext.ScenarioContext>());

    // There is no event for recognizing end of feature in Cucumber.
    // This map is used to record the last scenario time and its feature uri.
    // End of feature occurs once launch is finished.
    private Map<String, Date> featureEndTime = Collections.synchronizedMap(new HashMap<String, Date>());

    protected boolean enabled = Boolean.parseBoolean(System.getProperty("rp.enabled", "false"));

    /**
     * Registers an event handler for a specific event.
     * <p>
     * The available events types are:
     * <ul>
     * <li>{@link cucumber.api.event.TestRunStarted} - the first event sent.
     * <li>{@link cucumber.api.event.TestSourceRead} - sent for each feature file read, contains the feature file source.
     * <li>{@link cucumber.api.event.TestCaseStarted} - sent before starting the execution of a Test Case(/Pickle/Scenario),
     * contains the Test Case
     * <li>{@link cucumber.api.event.TestStepStarted} - sent before starting the execution of a Test Step, contains the Test Step
     * <li>{@link cucumber.api.event.TestStepFinished} - sent after the execution of a Test Step, contains the Test Step and
     * its Result.
     * <li>{@link cucumber.api.event.TestCaseFinished} - sent after the execution of a Test Case(/Pickle/Scenario), contains
     * the Test Case and its
     * Result.
     * <li>{@link cucumber.api.event.TestRunFinished} - the last event sent.
     * <li>{@link cucumber.api.event.EmbedEvent} - calling scenario.embed in a hook triggers this event.
     * <li>{@link cucumber.api.event.WriteEvent} - calling scenario.write in a hook triggers this event.
     * </ul>
     */
    @Override
    public void setEventPublisher(EventPublisher publisher) {
        if (enabled) {
            publisher.registerHandlerFor(TestRunStarted.class, getTestRunStartedHandler());
            publisher.registerHandlerFor(TestSourceRead.class, getTestSourceReadHandler());
            publisher.registerHandlerFor(TestCaseStarted.class, getTestCaseStartedHandler());
            publisher.registerHandlerFor(TestStepStarted.class, getTestStepStartedHandler());
            publisher.registerHandlerFor(TestStepFinished.class, getTestStepFinishedHandler());
            publisher.registerHandlerFor(TestCaseFinished.class, getTestCaseFinishedHandler());
            publisher.registerHandlerFor(TestRunFinished.class, getTestRunFinishedHandler());
            publisher.registerHandlerFor(EmbedEvent.class, getEmbedEventHandler());
            publisher.registerHandlerFor(WriteEvent.class, getWriteEventHandler());
        }
    }

    RunningContext.ScenarioContext getCurrentScenarioContext() {
        return threadCurrentScenarioContextMap.get(Thread.currentThread().getId());
    }

    /**
     * Manipulations before the launch starts
     */
    protected void beforeLaunch() {
        startLaunch();
    }

    /**
     * Finish RP launch
     */
    protected void afterLaunch() {
        FinishExecutionRQ finishLaunchRq = new FinishExecutionRQ();
        finishLaunchRq.setEndTime(Calendar.getInstance().getTime());
        launch.get().finish(finishLaunchRq);
    }

    /**
     * Start Cucumber scenario
     */
    private void beforeScenario(RunningContext.FeatureContext currentFeatureContext,
                                RunningContext.ScenarioContext currentScenarioContext) {
        Maybe<String> id = Utils.startNonLeafNode(
                launch.get(),
                currentFeatureContext.getFeatureId(),
                currentScenarioContext.getScenarioName(),
                currentScenarioContext.getDescription(),
                currentScenarioContext.getTags(),
                getScenarioTestItemType());
        currentScenarioContext.setId(id);
    }

    /**
     * Finish Cucumber scenario
     * Put scenario end time in a map to check last scenario end time per feature
     */
    private void afterScenario(TestCaseFinished event) {
        RunningContext.ScenarioContext currentScenarioContext = getCurrentScenarioContext();
        for (Map.Entry<ImmutablePair<String, String>, RunningContext.ScenarioContext> scenarioContext : currentScenarioContextMap
                .entrySet()) {
            if (scenarioContext.getValue().getLine() == currentScenarioContext.getLine()) {
                currentScenarioContextMap.remove(scenarioContext.getKey());
                Date endTime = Utils.finishTestItem(launch.get(), currentScenarioContext.getId(),
                        event.result.getStatus().toString());
                String featureURI = scenarioContext.getKey().getValue();
                featureEndTime.put(featureURI, endTime);
                break;
            }
        }
    }

    /**
     * It's essential to operate with feature URI,
     * to prevent problems with the same feature names in different folders/packages
     */
    private String buildFeatureNode(TestCase testCase) {
        RunningContext.FeatureContext featureContext = new RunningContext.FeatureContext()
                .processTestSourceReadEvent(testCase);
        String featureKeyword = featureContext.getFeature().getKeyword();
        String featureName = featureContext.getFeature().getName();
        Utils.buildNodeName(featureKeyword, AbstractReporter.COLON_INFIX, featureName, null);
        return featureContext.getUri();
    }

    /**
     * Start RP launch
     */
    private void startLaunch() {
        launch = Suppliers.memoize(new Supplier<Launch>() {

            /* should no be lazy */
            private final Date startTime = Calendar.getInstance().getTime();

            @Override
            public Launch get() {
                final ReportPortal reportPortal = ReportPortal.builder().build();
                ListenerParameters parameters = reportPortal.getParameters();

                StartLaunchRQ rq = new StartLaunchRQ();
                rq.setName(parameters.getLaunchName());
                rq.setStartTime(startTime);
                rq.setMode(parameters.getLaunchRunningMode());
                rq.setTags(parameters.getTags());
                rq.setDescription(parameters.getDescription());

                return reportPortal.newLaunch(rq);
            }
        });
    }

    /**
     * Start Cucumber step
     *
     * @param step Step object
     */
    protected abstract void beforeStep(TestStep step);

    /**
     * Finish Cucumber step
     *
     * @param result Step result
     */
    protected abstract void afterStep(Result result);

    /**
     * Called when before/after-hooks are started
     *
     * @param isBefore - if true, before-hook is started, if false - after-hook
     */
    protected abstract void beforeHooks(Boolean isBefore);

    /**
     * Called when before/after-hooks are finished
     *
     * @param isBefore - if true, before-hook is finished, if false - after-hook
     */
    protected abstract void afterHooks(Boolean isBefore);

    /**
     * Called when a specific before/after-hook is finished
     *
     * @param step     TestStep object
     * @param result   Hook result
     * @param isBefore - if true, before-hook, if false - after-hook
     */
    protected abstract void hookFinished(TestStep step, Result result, Boolean isBefore);

    /**
     * Return RP launch test item name mapped to Cucumber feature
     *
     * @return test item name
     */
    protected abstract String getFeatureTestItemType();

    /**
     * Return RP launch test item name mapped to Cucumber scenario
     *
     * @return test item name
     */
    protected abstract String getScenarioTestItemType();

    /**
     * Report test item result and error (if present)
     *
     * @param result  - Cucumber result object
     * @param message - optional message to be logged in addition
     */
    void reportResult(Result result, String message) {
        String cukesStatus = result.getStatus().toString();
        String level = Utils.mapLevel(cukesStatus);
        String errorMessage = result.getErrorMessage();
        if (errorMessage != null) {
            Utils.sendLog(errorMessage, level, null);
        }
    }

    private void embedding(String mimeType, byte[] data) {
        SaveLogRQ.File file = new SaveLogRQ.File();
        file.setName("embeddingName" + new Date().getTime() + new Random().nextInt(5000));
        file.setContent(data);
        Utils.sendLog("", "UNKNOWN", file);
    }

    private void write(String text) {
        if (StringUtils.containsIgnoreCase(text, "modifyTag")) {
            updateTag(text.replace("modifyTag:", ""));
        } else {
            Utils.sendLog(text, "UNKNOWN", null);
        }
    }

    private void updateTag(String tag) {
        JsonObject jsonElement = new JsonObject();

        List<String> tags = new ArrayList<>(getCurrentScenarioContext().getTags());
        tags.add(tag);

        jsonElement.addProperty("description", getCurrentScenarioContext().getDescription());
        jsonElement.addProperty("tags", new Gson().toJson(tags));

        String basePath = PropertiesLoader.load().getProperties().get("rp.endpoint").toString();

        Response response = given()
                .contentType("multipart/form-data")
                .header(new Header("Authorization", "Basic dWk6dWltYW4="))
                .header(new Header("Accept-Language", "nl"))
                .multiPart("username", "superadmin")
                .multiPart("password", "erebus")
                .multiPart("grant_type", "password")
                .request("POST", basePath + "/uat/sso/oauth/token");
        try {

            JsonNode responseBody = new ObjectMapper().readTree(response.getBody().asString());

            String accessToken = responseBody.get("access_token").asText();

            given()
                    .relaxedHTTPSValidation()
                    .header(new Header("Authorization", "bearer " + accessToken))
                    .body(jsonElement.toString()
                            .replace("\\", "")
                            .replace("\"[", "[")
                            .replace("]\"", "]"))
                    .contentType(ContentType.JSON)
                    .put(basePath + "/api/v1/test_project/item/" +
                            getCurrentScenarioContext().getId().blockingGet() +
                            "/update");
        } catch (IOException e) {
            //If this is thrown it means there is no API response.
        }
    }

    private boolean isBefore(TestStep step) {
        return HookType.Before == ((HookTestStep) step).getHookType();
    }

    protected abstract Maybe<String> getRootItemId();

    /**
     * Private part that responsible for handling events
     */

    private EventHandler<TestRunStarted> getTestRunStartedHandler() {
        return new EventHandler<TestRunStarted>() {
            @Override
            public void receive(TestRunStarted event) {
                beforeLaunch();
            }
        };
    }

    private EventHandler<TestSourceRead> getTestSourceReadHandler() {
        return new EventHandler<TestSourceRead>() {
            @Override
            public void receive(TestSourceRead event) {
                RunningContext.FeatureContext.addTestSourceReadEvent(event.uri, event);
            }
        };
    }

    private EventHandler<TestCaseStarted> getTestCaseStartedHandler() {
        return new EventHandler<TestCaseStarted>() {
            @Override
            public void receive(TestCaseStarted event) {
                handleStartOfTestCase(event);
            }
        };
    }

    private EventHandler<TestStepStarted> getTestStepStartedHandler() {
        return new EventHandler<TestStepStarted>() {
            @Override
            public void receive(TestStepStarted event) {
                handleTestStepStarted(event);
            }
        };
    }

    private EventHandler<TestStepFinished> getTestStepFinishedHandler() {
        return new EventHandler<TestStepFinished>() {
            @Override
            public void receive(TestStepFinished event) {
                handleTestStepFinished(event);
            }
        };
    }

    private EventHandler<TestCaseFinished> getTestCaseFinishedHandler() {
        return new EventHandler<TestCaseFinished>() {
            @Override
            public void receive(TestCaseFinished event) {
                afterScenario(event);
            }
        };
    }

    private EventHandler<TestRunFinished> getTestRunFinishedHandler() {
        return new EventHandler<TestRunFinished>() {
            @Override
            public void receive(TestRunFinished event) {
                handleEndOfFeature();
                afterLaunch();
            }
        };
    }

    private EventHandler<EmbedEvent> getEmbedEventHandler() {
        return new EventHandler<EmbedEvent>() {
            @Override
            public void receive(EmbedEvent event) {
                embedding(event.mimeType, event.data);
            }
        };
    }

    private EventHandler<WriteEvent> getWriteEventHandler() {
        return new EventHandler<WriteEvent>() {
            @Override
            public void receive(WriteEvent event) {
                write(event.text);
            }
        };
    }

    private void handleEndOfFeature() {
        for (RunningContext.FeatureContext value : currentFeatureContextMap.values()) {
            Date featureCompletionDateTime = featureEndTime.get(value.getUri());
            Utils.finishFeature(launch.get(), value.getFeatureId(), featureCompletionDateTime);
        }
        currentFeatureContextMap.clear();
    }

    private void handleStartOfTestCase(TestCaseStarted event) {
        TestCase testCase = event.testCase;
        String featureURI = buildFeatureNode(testCase);
        RunningContext.FeatureContext currentFeatureContext = currentFeatureContextMap.get(featureURI);

        currentFeatureContext = currentFeatureContext == null ?
                                createFeatureContext(testCase, featureURI) : currentFeatureContext;

        if (!currentFeatureContext.getUri().equals(testCase.getUri())) {
            throw new IllegalStateException("Scenario URI does not match Feature URI.");
        }

        RunningContext.ScenarioContext scenarioContext = currentFeatureContext.getScenarioContext(testCase);
        String scenarioName = Utils.buildNodeName(scenarioContext.getKeyword(), AbstractReporter.COLON_INFIX,
                scenarioContext.getName(), scenarioContext.getOutlineIteration());

        ImmutablePair<String, String> scenarioNameFeatureURI =
                new ImmutablePair<String, String>(scenarioName, currentFeatureContext.getUri());
        RunningContext.ScenarioContext currentScenarioContext = currentScenarioContextMap.get(scenarioNameFeatureURI);

        if (currentScenarioContext == null) {
            currentScenarioContext = currentFeatureContext.getScenarioContext(testCase);
            currentScenarioContextMap.put(scenarioNameFeatureURI, currentScenarioContext);
            threadCurrentScenarioContextMap.put(Thread.currentThread().getId(), currentScenarioContext);
        }

        beforeScenario(currentFeatureContext, currentScenarioContext);
    }

    private RunningContext.FeatureContext createFeatureContext(TestCase testCase, String featureURI) {
        RunningContext.FeatureContext currentFeatureContext;
        currentFeatureContext = new RunningContext.FeatureContext().processTestSourceReadEvent(testCase);
        currentFeatureContextMap.put(featureURI, currentFeatureContext);
        String featureKeyword = currentFeatureContext.getFeature().getKeyword();
        String featureName = currentFeatureContext.getFeature().getName();

        StartTestItemRQ rq = new StartTestItemRQ();
        Maybe<String> root = getRootItemId();
        rq.setDescription(currentFeatureContext.getFeature().getDescription());
        rq.setName(Utils.buildNodeName(featureKeyword, AbstractReporter.COLON_INFIX, featureName, null));
        rq.setTags(currentFeatureContext.getTags());
        rq.setStartTime(Calendar.getInstance().getTime());
        rq.setType(getFeatureTestItemType());
        currentFeatureContext.setFeatureId(root == null ?
                                           launch.get().startTestItem(rq) : launch.get().startTestItem(root, rq));
        return currentFeatureContext;
    }

    private void handleTestStepStarted(TestStepStarted event) {
        TestStep testStep = event.testStep;
        if (testStep instanceof HookTestStep) {
            beforeHooks(isBefore(testStep));
        } else {
            if (getCurrentScenarioContext().withBackground()) {
                getCurrentScenarioContext().nextBackgroundStep();
            }
            beforeStep(testStep);
        }
    }

    private void handleTestStepFinished(TestStepFinished event) {
        if (event.testStep instanceof HookTestStep) {
            hookFinished(event.testStep, event.result, isBefore(event.testStep));
            afterHooks(isBefore(event.testStep));
        } else {
            afterStep(event.result);
        }
    }
}