package be.refleqt.selenium.driver.setup.PropertiesLoaders;

import be.refleqt.selenium.driver.DriverProvider;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import org.springframework.core.io.ClassPathResource;
import org.testng.Assert;

public class BrowserPropertiesLoader {

    private static BrowserPropertiesLoader instance;

    private Properties properties;

    private BrowserPropertiesLoader() {
        this.load();
    }

    private void load() {
        if (properties == null) {
            try {
                this.properties = new Properties();
                properties.load(new ClassPathResource("browsers" + File.separator + DriverProvider.getBrowser().toString() + ".properties").getInputStream());

                for (String propertyName : properties.stringPropertyNames()) {
                    String systemPropertyValue = System.getProperty(propertyName);

                    if (systemPropertyValue != null) {
                        properties.setProperty(propertyName, systemPropertyValue);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                Assert.fail("Failed to load properties for " + DriverProvider.getBrowser().toString());
            }
        }
    }

    public static BrowserPropertiesLoader getInstance() {
        if (instance == null) {
            instance = new BrowserPropertiesLoader();
        }
        return instance;
    }

    public List<String> getProperties() {
        return properties.values().stream().map(Object::toString).collect(Collectors.toList());
    }
}
