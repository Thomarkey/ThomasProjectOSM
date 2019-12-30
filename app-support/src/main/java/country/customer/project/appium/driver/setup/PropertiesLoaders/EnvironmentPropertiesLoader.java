package country.customer.project.appium.driver.setup.PropertiesLoaders;

import country.customer.project.appium.driver.setup.enums.EnvironmentType;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import static org.assertj.core.api.Assertions.assertThat;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.core.io.ClassPathResource;

public class EnvironmentPropertiesLoader {

    private static ThreadLocal<EnvironmentPropertiesLoader> instance = new ThreadLocal<>();

    private Properties properties;
    private DesiredCapabilities desiredCapabilities;

    private EnvironmentPropertiesLoader(DesiredCapabilities desiredCapabilities) {
        this.desiredCapabilities = desiredCapabilities;
        this.load();
    }

    private void load() {
        if (properties == null) {
            try {
                this.properties = new Properties();
                properties.load(new ClassPathResource(
                        "environments" +
                                File.separator +
                                EnvironmentType.fromString(desiredCapabilities.getCapability("executionMode")).toString() +
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
                assertThat("Failed to load properties for " + System.getProperty("executionMode")).isBlank();
            }
        }
    }

    public static EnvironmentPropertiesLoader getInstance(DesiredCapabilities desiredCapabilities) {
        if (instance.get() == null) {
            instance.set(new EnvironmentPropertiesLoader(desiredCapabilities));
        }
        return instance.get();
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
