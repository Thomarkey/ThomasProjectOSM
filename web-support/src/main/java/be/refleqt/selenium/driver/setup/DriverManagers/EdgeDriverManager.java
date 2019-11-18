package be.refleqt.selenium.driver.setup.DriverManagers;

import org.openqa.selenium.*;
import org.openqa.selenium.edge.*;
import org.openqa.selenium.remote.*;

public class EdgeDriverManager extends DriverManager {

    @Override
    public WebDriver getDriver(DesiredCapabilities desiredCapabilities) {
        if (driver == null) {
            createWebDriver(desiredCapabilities);
        }
        return driver;
    }

    @Override
    public void createWebDriver(DesiredCapabilities desiredCapabilities) {
        this.driver = new EdgeDriver(new EdgeOptions().merge(desiredCapabilities));
    }
}
