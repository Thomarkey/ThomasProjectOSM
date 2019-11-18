package be.refleqt.selenium.driver.setup.DriverManagers;

import be.refleqt.selenium.driver.setup.PropertiesLoaders.*;
import java.util.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.remote.*;

public class ChromeDriverManager extends DriverManager {

    private ChromeOptions chromeOptions;

    public ChromeDriverManager() {
    }

    @Override
    public WebDriver getDriver(DesiredCapabilities desiredCapabilities) {
        if (driver == null) {
            createWebDriver(desiredCapabilities);
        }
        return driver;
    }

    @Override
    public void createWebDriver(DesiredCapabilities desiredCapabilities) {
        this.driver = new ChromeDriver(getChromeOptions().merge(desiredCapabilities));
    }

    public ChromeOptions getChromeOptions() {
        chromeOptions = new ChromeOptions();

        setExperimentalOption("prefs", BrowserExperimentalLoader.getInstance().getProperties());
        BrowserPropertiesLoader.getInstance().getProperties().forEach(this::setOptionsArgument);

        return chromeOptions;
    }

    private void setOptionsArgument(String arg) {
        if (chromeOptions != null) {
            chromeOptions.addArguments(arg);
        } else {
            throw new RuntimeException("Create the WebDriver first before trying to add options to it!");
        }
    }

    private void setExperimentalOption(String arg, Map<String, Object> options) {
        if (chromeOptions != null) {
            chromeOptions.setExperimentalOption(arg, options);
        } else {
            throw new RuntimeException("Create the WebDriver first before trying to add options to it!");
        }
    }
}
