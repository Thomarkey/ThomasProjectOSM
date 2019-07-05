package be.refleqt.projectname.tests;

import be.refleqt.library.selenium.driver.setup.DockerProvider;
import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import cucumber.api.testng.AbstractTestNGCucumberTests;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

@CucumberOptions(
        features = "src/test/resources/features/",
        glue = "be.refleqt.projectname.steps",
        plugin = {"pretty", "json:target/cucumber/json/full.json", "html:target/cucumber/html/full/"},
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
