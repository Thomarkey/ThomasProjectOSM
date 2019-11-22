package be.refleqt.selenium.support;

import be.refleqt.general.support.GenericScenarioManager;
import be.refleqt.selenium.driver.DriverProvider;

public class ScenarioManager extends GenericScenarioManager {

    public static void saveScreenshot() {
        byte[] screenshot = DriverProvider.getScaledScreenshot(3);

        if (screenshot != null) {
            scenario.get().embed(screenshot, "image/png");
        }
    }
}
