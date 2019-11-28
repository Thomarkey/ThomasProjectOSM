package country.customer.project.selenium.driver.setup.DriverManagers;

import country.customer.project.selenium.driver.setup.PropertiesLoaders.EnvironmentPropertiesLoader;
import country.customer.project.selenium.driver.setup.enums.BrowserType;
import country.customer.project.selenium.support.ScenarioManager;
import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class RemoteDriverManager extends DriverManager {

    private BrowserType browser;

    public RemoteDriverManager(BrowserType browser) {
        this.browser = browser;
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
        DesiredCapabilities cloudCapabilities = new DesiredCapabilities(
                browser.toString(),
                System.getProperty("version", ""),
                Platform.fromString(System.getProperty("platform", "any"))
        );

        String url = String.format(
                EnvironmentPropertiesLoader.getInstance().getProperty("url"),
                EnvironmentPropertiesLoader.getInstance().getProperty("username"),
                EnvironmentPropertiesLoader.getInstance().getProperty("accessKey")
        );

        cloudCapabilities.setCapability(
                "resolution",
                System.getProperty("resolution", "1920x1080")
        );

        cloudCapabilities.setCapability(
                "name",
                "Scenario name: " + ScenarioManager.getScenario().getName()
        );

        if (browser.equals(BrowserType.CHROME)) {
            cloudCapabilities.merge(new ChromeDriverManager().getChromeOptions());
        }

        try {
            this.driver = new RemoteWebDriver(new URL(url), cloudCapabilities.merge(desiredCapabilities));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
