package be.refleqt.projectname.tests;

import be.refleqt.library.selenium.DriverProvider;
import be.refleqt.library.selenium.environments.DockerProvider;
import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import cucumber.api.testng.AbstractTestNGCucumberTests;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

@CucumberOptions(
        features = "src/test/resources/features/",
        tags = "@wip",
        glue = "be.refleqt.projectname.steps",
        plugin = {"pretty", "json:target/cucumber/json/full.json", "html:target/cucumber/html/full/"},
        strict = true,
        snippets = SnippetType.CAMELCASE
)
public class Local extends AbstractTestNGCucumberTests {

    @BeforeSuite
    public static void setupDocker() {
        DockerProvider.getInstance().setupStandAloneChrome();
    }

    @AfterSuite
    public static void tearDownDocker() {
        DockerProvider.getInstance().teardownStandAlone();
    }
}
