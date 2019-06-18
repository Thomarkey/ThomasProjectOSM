package be.refleqt.projectname.tests;

import be.refleqt.library.selenium.driver.setup.DockerProvider;
import org.testng.annotations.*;

public class TestExecutor {

    @Test(description = "Runs Cucumber Feature")
    @Parameters({"cucumberTag", "threads"})
    public void executeTest(@Optional("wip") String cucumberTag, @Optional("4") String threads) {
        String arg = "src/test/resources/features/ --threads " + threads +" --plugin json:target/cucumber-report/test.json " +
                "--plugin html:target/cucumber-report/html " +
                "-t @" + cucumberTag + " --strict --glue be.refleqt.projectname.steps";
        String[] args = arg.split(" ");

        cucumber.api.cli.Main.run(args, Thread.currentThread().getContextClassLoader());
    }

    @BeforeSuite
    public static void setupDocker() {
        DockerProvider.getInstance().setupDockerGrid(4);
    }

    @AfterSuite
    public static void tearDownDocker() {
        DockerProvider.getInstance().tearDownDockerGrid();
    }
}
