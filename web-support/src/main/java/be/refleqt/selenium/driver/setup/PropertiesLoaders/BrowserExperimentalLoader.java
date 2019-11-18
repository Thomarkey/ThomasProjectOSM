package be.refleqt.selenium.driver.setup.PropertiesLoaders;

import be.refleqt.selenium.driver.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import org.springframework.core.io.*;
import org.testng.*;

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
