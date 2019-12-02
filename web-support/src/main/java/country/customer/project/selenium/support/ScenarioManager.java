package country.customer.project.selenium.support;

import country.customer.project.general.support.GenericScenarioManager;
import country.customer.project.selenium.driver.DriverProvider;

public class ScenarioManager extends GenericScenarioManager {

    public static void saveScreenshot() {
        byte[] screenshot = DriverProvider.getScaledScreenshot(3);

        if (screenshot != null) {
            scenario.get().embed(screenshot, "image/png");
        }
    }
}
