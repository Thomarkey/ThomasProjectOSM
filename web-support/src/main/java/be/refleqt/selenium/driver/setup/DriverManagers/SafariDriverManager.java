package be.refleqt.selenium.driver.setup.DriverManagers;

import org.openqa.selenium.*;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.safari.*;

public class SafariDriverManager extends DriverManager {

    @Override
    public WebDriver getDriver(DesiredCapabilities desiredCapabilities) {
        if (driver == null) {
            createWebDriver(desiredCapabilities);
        }
        return driver;
    }

    @Override
    public void createWebDriver(DesiredCapabilities desiredCapabilities) {
        this.driver = new SafariDriver(new SafariOptions().merge(desiredCapabilities));
    }
}
