package country.customer.project.selenium.support;

import country.customer.project.general.support.GenericScenarioManager;
import country.customer.project.selenium.driver.DriverProvider;

public class ScenarioManager extends GenericScenarioManager {

    public static void saveScreenshot() {
        // You can adjust `scaled.screenshot` in web-testing module. See file `project.properties`
        byte[] screenshot = DriverProvider.getScaledScreenshot(Integer.parseInt(PropertiesReader.getKey("scaled.screenshot")));

        if (screenshot != null) {
            scenario.get().embed(screenshot, "image/png");
        }
    }
}
