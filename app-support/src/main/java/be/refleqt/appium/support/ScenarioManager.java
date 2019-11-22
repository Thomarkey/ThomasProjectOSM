package be.refleqt.appium.support;

import be.refleqt.appium.driver.DriverProvider;
import be.refleqt.general.support.GenericScenarioManager;

public class ScenarioManager extends GenericScenarioManager {

    public static void saveScreenshot() {
        byte[] screenshot = DriverProvider.getScaledScreenshot(3);

        if (screenshot != null) {
            scenario.get().embed(screenshot, "image/png");
        }
    }
}
