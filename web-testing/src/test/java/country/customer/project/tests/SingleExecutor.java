package country.customer.project.tests;

import country.customer.project.selenium.driver.setup.DockerProvider;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.CucumberOptions.SnippetType;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

@CucumberOptions(
        features = "src/test/resources/features/",
        glue = "country.customer.project.steps",
        plugin = {
                "progress", "json:target/cucumber/json/full.json", "html:target/cucumber/html/full/",
                "country.customer.project.general.support.reportportal.agent.ScenarioReporter"
        },
        strict = true,
        snippets = SnippetType.CAMELCASE
)
public class SingleExecutor extends AbstractTestNGCucumberTests {

    /**
     * Docker is only setup if System property "executionMode" is a docker option
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
