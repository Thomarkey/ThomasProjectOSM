package be.refleqt.selenium.driver.setup.DriverManagers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class DriverManager {

    WebDriver driver;

    public void createWebDriver(DesiredCapabilities desiredCapabilities) {
    }

    public WebDriver getDriver(DesiredCapabilities desiredCapabilities) {
        if (driver != null) {
            createWebDriver(desiredCapabilities);
        }
        return driver;
    }
}
