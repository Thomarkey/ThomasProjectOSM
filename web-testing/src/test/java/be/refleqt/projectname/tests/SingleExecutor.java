package be.refleqt.projectname.tests;

import be.refleqt.selenium.driver.setup.*;
import io.cucumber.testng.*;
import io.cucumber.testng.CucumberOptions.*;
import org.testng.annotations.*;

@CucumberOptions(
        features = "src/test/resources/features/",
        glue = "be.refleqt.projectname.steps",
        plugin = {
                "progress", "json:target/cucumber/json/full.json", "html:target/cucumber/html/full/",
                "be.refleqt.general.support.reportportal.agent.ScenarioReporter"
        },
        strict = true,
        snippets = SnippetType.CAMELCASE
)
public class SingleExecutor extends AbstractTestNGCucumberTests {

    /**
     * Docker is only setup if System property "env" is a docker option
     */
    @BeforeSuite
    public static void setupDocker() {
        System.out.println("Running the tag: " + System.getProperty("cucumberTag", "wip"));
        System.setProperty("cucumber.options", "--tags @" + System.getProperty("cucumberTag", "wip"));

        DockerProvider.getInstance().setupStandAlone();
    }

    @AfterSuite
    public static void tearDownDocker() {
        DockerProvider.getInstance().teardownStandAlone();
    }
}
