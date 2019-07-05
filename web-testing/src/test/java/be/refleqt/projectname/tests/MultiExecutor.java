package be.refleqt.projectname.tests;

import be.refleqt.library.selenium.driver.setup.DockerProvider;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class MultiExecutor {

    @Test(description = "Runs Cucumber Feature")
    public void executeTest() {
        String tag = System.getProperty("cucumberTag", "wip");
        System.out.println("Running tag: " + tag);
        String arg = "src/test/resources/features/ --threads " + System.getProperty("threads", "4") +
                " --plugin json:target/cucumber-report/test.json " +
                "--plugin html:target/cucumber-report/html " +
                "-t @" + tag + " --strict --glue be.refleqt.projectname.steps";
        String[] args = arg.split(" ");

        cucumber.api.cli.Main.run(args, Thread.currentThread().getContextClassLoader());
    }

    /**
     * Docker is only setup if System property "env" is a docker option
     */
    @BeforeSuite
    public static void setupDocker() {
        DockerProvider.getInstance().setupDockerGrid(System.getProperty("threads", "4"));
    }

    @AfterSuite
    public static void tearDownDocker() {
        DockerProvider.getInstance().tearDownDockerGrid();
    }
}
