package country.customer.project.appium.driver.setup;

import country.customer.project.appium.driver.setup.DriverManagers.DriverManager;
import country.customer.project.appium.driver.setup.DriverManagers.RemoteDriverManager;
import country.customer.project.appium.driver.setup.enums.EnvironmentType;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

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
