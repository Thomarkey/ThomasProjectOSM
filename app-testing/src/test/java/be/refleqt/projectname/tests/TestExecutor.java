package be.refleqt.projectname.tests;

import be.refleqt.library.appium.DriverProvider;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;

public class TestExecutor {

    @Test(description = "Runs Cucumber Feature")
    @Parameters({"device"})
    public void executeTest(@Optional String device) {
        String tag = System.getProperty("cucumberTag", "wip");
        System.out.println("Running tag: " + tag);
        String arg = "src/test/resources/features/ --threads " + System.getProperty("threads", "1") +
                " --plugin json:target/cucumber-report/" + device + ".json " +
                "--plugin html:target/cucumber-report/html " +
                "-t @" + tag + " --strict --glue be.refleqt.projectname.steps";
        String[] args = arg.split(" ");

        if (device != null) {
            DriverProvider.setDevice(device);
        }

        byte exitCode = io.cucumber.core.cli.Main.run(args, Thread.currentThread().getContextClassLoader());

        if (exitCode != 0) {
            Reporter.getCurrentTestResult().setStatus(ITestResult.FAILURE);
        }
    }

    @BeforeSuite
    public static void startAppium() {
        DriverProvider.startAppiumServer();
    }

    @AfterSuite
    public static void stopAppium() {
        DriverProvider.stopAppiumServer();
    }
}
