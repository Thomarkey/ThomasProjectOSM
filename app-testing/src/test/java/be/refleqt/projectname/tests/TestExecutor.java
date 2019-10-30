package be.refleqt.projectname.tests;

import be.refleqt.library.appium.*;
import org.testng.*;
import org.testng.annotations.*;

public class TestExecutor {

    @Test(description = "Runs Cucumber Feature",
            dataProvider = "getDevices",
            dataProviderClass = CommonDataProvider.class)
    public void executeTest(String device) {
        String tag = System.getProperty("cucumberTag", "wip");
        System.out.println("Running tag: " + tag);
        String arg = "src/test/resources/features/ --threads " + System.getProperty("threads", "1") +
                " --plugin json:target/cucumber-report/" + device + ".json " +
                "--plugin html:target/cucumber-report/+" + device + " " +
                "--plugin junit:target/surefire-reports/TEST-" + device + ".xml " +
                "-t @" + tag + " --strict --glue be.refleqt.projectname.steps";
        String[] args = arg.split(" ");

        DriverProvider.setDevice(device);

        byte exitCode = io.cucumber.core.cli.Main.run(args, Thread.currentThread().getContextClassLoader());

        if (exitCode != 0) {
            Reporter.getCurrentTestResult().setStatus(ITestResult.FAILURE);
        }
    }

    @BeforeSuite
    public static void startAppium() {
        DriverProvider.startAppiumServer();
    }

    @AfterSuite(alwaysRun = true)
    public static void stopAppium() {
        DriverProvider.stopAppiumServer();
    }
}
