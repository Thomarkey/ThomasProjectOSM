package be.refleqt.selenium.driver.setup.DriverManagers;

import org.openqa.selenium.*;
import org.openqa.selenium.remote.*;

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
