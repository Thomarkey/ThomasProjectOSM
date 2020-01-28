package country.customer.project.selenium.support;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {
    /**
     * Members
     */
    private static final Properties PROPERTIES = new Properties();

    /** Methods */
    static {
        try {
            InputStream stream = PropertiesReader.class.getResourceAsStream("/project.properties");
            PROPERTIES.load(stream);
            stream.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("ProjectProperties cannot be loaded: " + e.getLocalizedMessage());
        }
    }

    private PropertiesReader() {
    }

    public static String getKey(String aKey) {
        return PROPERTIES.getProperty(aKey);
    }

    public static void setKey(String aKey, String value) {
        PROPERTIES.setProperty(aKey, value);
    }
}
