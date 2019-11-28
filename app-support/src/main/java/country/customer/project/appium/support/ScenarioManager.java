package country.customer.project.appium.support;

import country.customer.project.appium.driver.DriverProvider;
import country.customer.project.general.support.GenericScenarioManager;

public class ScenarioManager extends GenericScenarioManager {

    public static void saveScreenshot() {
        byte[] screenshot = DriverProvider.getScaledScreenshot(3);

        if (screenshot != null) {
            scenario.get().embed(screenshot, "image/png");
        }
    }
}
