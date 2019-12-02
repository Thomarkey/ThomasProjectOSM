package country.customer.project.tests;

import country.customer.project.selenium.driver.DriverProvider;
import country.customer.project.selenium.driver.setup.DockerProvider;
import country.customer.project.selenium.support.CommonDataProvider;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;

public class MultiExecutor {

    @Test(description = "Runs Cucumber Feature",
            dataProvider = "getBrowsers",
            dataProviderClass = CommonDataProvider.class)
    public void executeTest(String browser) {
        String tag = System.getProperty("cucumberTag", "wip");
        String arg = "src/test/resources/features/ --threads " + System.getProperty("threads", "2") +
                " --plugin json:target/cucumber-report/" + browser + ".json " +
                "--plugin html:target/cucumber-report/" + browser + " " +
                "--plugin junit:target/surefire-reports/TEST-" + browser + ".xml " +
                "--plugin country.customer.project.general.support.reportportal.agent.ScenarioReporter " +
                "--plugin progress " +
                "-t @" + tag + " --strict --glue country.customer.project.steps";
        String[] args = arg.split(" ");
        DriverProvider.setBrowser(browser);

        byte exitCode = io.cucumber.core.cli.Main.run(args, Thread.currentThread().getContextClassLoader());

        if (exitCode != 0) {
            Reporter.getCurrentTestResult().setStatus(ITestResult.FAILURE);
        }
    }

    /**
     * Docker is only setup if System property "env" is a docker option
     */
    @BeforeSuite
    public static void setupDocker() {
        DockerProvider.getInstance().setupDockerGrid(System.getProperty("threads", "2"));
    }

    @AfterSuite(alwaysRun = true)
    public static void tearDownDocker() {
        DockerProvider.getInstance().tearDownDockerGrid();
    }
}
