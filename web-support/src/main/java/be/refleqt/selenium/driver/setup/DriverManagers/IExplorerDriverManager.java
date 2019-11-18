package be.refleqt.selenium.driver.setup.DriverManagers;

import org.openqa.selenium.*;
import org.openqa.selenium.ie.*;
import org.openqa.selenium.remote.*;

public class IExplorerDriverManager extends DriverManager {

    @Override
    public WebDriver getDriver(DesiredCapabilities desiredCapabilities) {
        if (driver == null) {
            createWebDriver(desiredCapabilities);
        }
        return driver;
    }

    @Override
    public void createWebDriver(DesiredCapabilities desiredCapabilities) {
        this.driver = new InternetExplorerDriver(new InternetExplorerOptions().merge(desiredCapabilities));
    }
}
