package be.refleqt.projectname.tests;

import org.testng.ITestResult;
import org.testng.Reporter;
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

        byte exitCode = io.cucumber.core.cli.Main.run(args, Thread.currentThread().getContextClassLoader());

        if (exitCode != 0) {
            Reporter.getCurrentTestResult().setStatus(ITestResult.FAILURE);
        }
    }
}