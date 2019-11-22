package be.refleqt.selenium.driver.setup.PropertiesLoaders;

import be.refleqt.selenium.driver.DriverProvider;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import org.springframework.core.io.ClassPathResource;
import org.testng.Assert;

public class BrowserExperimentalLoader {

    private static BrowserExperimentalLoader instance;

    private Properties properties;

    private BrowserExperimentalLoader() {
        this.load();
    }

    private void load() {
        if (properties == null) {
            try {
                this.properties = new Properties();
                properties.load(new ClassPathResource("browsers" + File.separator + DriverProvider.getBrowser().toString() + ".experimental.properties").getInputStream());

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

    public static BrowserExperimentalLoader getInstance() {
        if (instance == null) {
            instance = new BrowserExperimentalLoader();
        }
        return instance;
    }

    public Map<String, Object> getProperties() {
        return properties.entrySet().stream().collect(Collectors.toMap(p -> p.getKey().toString(), Map.Entry::getValue));
    }
}
