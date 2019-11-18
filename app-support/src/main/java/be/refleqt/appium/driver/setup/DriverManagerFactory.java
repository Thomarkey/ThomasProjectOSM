package be.refleqt.appium.driver.setup;

import be.refleqt.appium.driver.setup.DriverManagers.*;
import be.refleqt.appium.driver.setup.enums.*;
import io.appium.java_client.*;
import org.openqa.selenium.remote.*;

public class DriverManagerFactory {

    public static AppiumDriver getDriver(DesiredCapabilities desiredCapabilities) {
        DriverManager driverManager = null;
        EnvironmentType environment = EnvironmentType.fromString(desiredCapabilities.getCapability("env"));

        switch (environment) {
            case DOCKER:
            case BROWSERSTACK:
            case SAUCELABS:
                driverManager = new RemoteDriverManager();
                break;
            case LOCAL:
            default:
                driverManager = new DriverManager();
        }

        return driverManager.getDriver(desiredCapabilities);
    }
}
