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

import cucumber.api.Result;
import cucumber.api.TestStep;
import gherkin.ast.Step;
import io.reactivex.Maybe;

public class ScenarioReporter extends AbstractReporter {

    @Override
    protected void beforeLaunch() {
        if (enabled) { super.beforeLaunch(); }
    }

    @Override
    protected void beforeStep(TestStep testStep) {
        RunningContext.ScenarioContext currentScenarioContext = getCurrentScenarioContext();
        Step step = currentScenarioContext.getStep(testStep);
        String decoratedStepName = decorateMessage(Utils.buildNodeName(currentScenarioContext.getStepPrefix(),
                step.getKeyword(), Utils.getStepName(testStep), " "));
        String multilineArg = Utils.buildMultilineArgument(testStep);
        Utils.sendLog(decoratedStepName + multilineArg, "DEBUG", null);
    }

    @Override
    protected void afterStep(Result result) {
        if (result.getErrorMessage() != null) {
            reportResult(result, decorateMessage("STEP " + result.getStatus().toString().toUpperCase()));
        }
    }

    @Override
    protected void beforeHooks(Boolean isBefore) {
        // noop
    }

    @Override
    protected void afterHooks(Boolean isBefore) {
        // noop
    }

    @Override
    protected void hookFinished(TestStep step, Result result, Boolean isBefore) {
        reportResult(result, (isBefore ? "@Before" : "@After") + "\n" + step.getCodeLocation());
    }

    @Override
    protected String getFeatureTestItemType() {
        return "TEST";
    }

    @Override
    protected String getScenarioTestItemType() {
        return "STEP";
    }

    @Override
    protected Maybe<String> getRootItemId() {
        return null;
    }

    @Override
    protected void afterLaunch() {
        if (enabled) { super.afterLaunch(); }
    }

    /**
     * Add separators to log item to distinguish from real log messages
     *
     * @param message to decorate
     *
     * @return decorated message
     */
    private String decorateMessage(String message) {
        return message;
    }
}