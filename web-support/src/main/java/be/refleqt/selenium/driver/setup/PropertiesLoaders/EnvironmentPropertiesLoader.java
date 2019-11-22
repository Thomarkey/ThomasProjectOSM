package be.refleqt.selenium.driver.setup.PropertiesLoaders;

import be.refleqt.selenium.driver.setup.enums.EnvironmentType;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import org.springframework.core.io.ClassPathResource;
import org.testng.Assert;

public class EnvironmentPropertiesLoader {

    private static EnvironmentPropertiesLoader instance;

    private Properties properties;

    private EnvironmentPropertiesLoader() {
        this.load();
    }

    private void load() {
        if (properties == null) {
            try {
                this.properties = new Properties();
                properties.load(new ClassPathResource(
                        "environments" +
                                File.separator +
                                EnvironmentType.fromString(System.getProperty("env", "local")).toString() +
                                ".properties"
                ).getInputStream());

                for (String propertyName : properties.stringPropertyNames()) {
                    String systemPropertyValue = System.getProperty(propertyName);

                    if (systemPropertyValue != null) {
                        properties.setProperty(propertyName, systemPropertyValue);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                Assert.fail("Failed to load properties for " + System.getProperty("env"));
            }
        }
    }

    public static EnvironmentPropertiesLoader getInstance() {
        if (instance == null) {
            instance = new EnvironmentPropertiesLoader();
        }
        return instance;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
