package country.customer.project.selenium.driver.setup.DriverManagers;

import country.customer.project.selenium.driver.setup.PropertiesLoaders.BrowserExperimentalLoader;
import country.customer.project.selenium.driver.setup.PropertiesLoaders.BrowserPropertiesLoader;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

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
