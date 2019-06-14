package be.refleqt.projectname.tests;

import be.refleqt.library.selenium.environments.DockerProvider;
import org.testng.annotations.*;

public class TestExecutor {

    @Test(description = "Runs Cucumber Feature")
    @Parameters({"cucumberTag"})
    public void executeTest(@Optional("wip") String cucumberTag) {
        String arg = "src/test/resources/features/ --threads 4 --plugin json:target/cucumber-report/test.json " +
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
