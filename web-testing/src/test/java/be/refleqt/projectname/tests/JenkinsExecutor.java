package be.refleqt.projectname.tests;

import be.refleqt.library.selenium.environments.DockerProvider;
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
public class JenkinsExecutor extends AbstractTestNGCucumberTests {

    @BeforeSuite
    public static void setupDocker() {
        if (System.getProperty("cucumberTags") != null) {
            System.out.println("Running the tag: " + System.getProperty("cucumberTags"));
            System.setProperty("cucumber.options", "--tags @" + System.getProperty("cucumberTags"));
        }

        DockerProvider.getInstance().setupStandAloneChrome();
    }

    @AfterSuite
    public static void tearDownDocker() {
        DockerProvider.getInstance().teardownStandAlone();
    }
}
