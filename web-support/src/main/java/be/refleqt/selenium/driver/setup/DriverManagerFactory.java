package be.refleqt.selenium.driver.setup;

import be.refleqt.selenium.driver.setup.DriverManagers.*;
import be.refleqt.selenium.driver.setup.enums.BrowserType;
import be.refleqt.selenium.driver.setup.enums.EnvironmentType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class DriverManagerFactory {

    public static WebDriver getDriver(BrowserType browser, EnvironmentType environment, DesiredCapabilities desiredCapabilities) {
        DriverManager driverManager = null;

        switch (environment) {
            case LOCAL:
                switch (browser) {
                    case EDGE:
                        driverManager = new EdgeDriverManager();
                        break;
                    case FIREFOX:
                        driverManager = new FireFoxDriverManager();
                        break;
                    case IEXPLORER:
                        driverManager = new IExplorerDriverManager();
                        break;
                    case SAFARI:
                        driverManager = new SafariDriverManager();
                        break;
                    case CHROME:
                    default:
                        driverManager = new ChromeDriverManager();
                        break;
                }
                break;
            case DOCKER:
            case BROWSERSTACK:
            case SAUCELABS:
                driverManager = new RemoteDriverManager(browser);
                break;
        }

        return driverManager.getDriver(desiredCapabilities);
    }
}
