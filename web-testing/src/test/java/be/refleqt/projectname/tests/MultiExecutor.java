package be.refleqt.projectname.tests;

import be.refleqt.library.selenium.DriverProvider;
import be.refleqt.library.selenium.driver.setup.DockerProvider;
import org.testng.annotations.*;

public class MultiExecutor {

    @Test(description = "Runs Cucumber Feature")
    @Parameters({"browser"})
    public void executeTest(@Optional String browser) {
        String tag = System.getProperty("cucumberTag", "wip");
        String arg = "src/test/resources/features/ --threads " + System.getProperty("threads", "2") +
                " --plugin json:target/cucumber-report/test.json " +
                "--plugin html:target/cucumber-report/html " +
                "-t @" + tag + " --strict --glue be.refleqt.projectname.steps";
        String[] args = arg.split(" ");

        if (browser != null) {
            DriverProvider.setBrowser(browser);
        }

        io.cucumber.core.cli.Main.run(args, Thread.currentThread().getContextClassLoader());
    }

    /**
     * Docker is only setup if System property "env" is a docker option
     */
    @BeforeSuite
    public static void setupDocker() {
        DockerProvider.getInstance().setupDockerGrid(System.getProperty("threads", "2"));
    }

    @AfterSuite
    public static void tearDownDocker() {
        DockerProvider.getInstance().tearDownDockerGrid();
    }
}
