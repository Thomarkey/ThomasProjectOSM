package be.refleqt.appium.driver.setup.DriverManagers;

import be.refleqt.appium.driver.*;
import be.refleqt.appium.driver.setup.util.*;
import be.refleqt.appium.support.*;
import io.appium.java_client.*;
import io.appium.java_client.android.*;
import io.appium.java_client.ios.*;
import org.openqa.selenium.remote.*;

public class DriverManager {

    AppiumDriver driver;

    public void createWebDriver(DesiredCapabilities desiredCapabilities) {
        DeviceManager.installApp(desiredCapabilities);

        switch (desiredCapabilities.getPlatform()) {
            case IOS:
                this.driver = new IOSDriver(DriverProvider.getServiceUrl(), desiredCapabilities);
                break;
            case ANDROID:
                DeviceManager.unLockAndroidDevice(
                        desiredCapabilities.getCapability("udid").toString(),
                        GenericDataHelper.getBooleanValue(desiredCapabilities.getCapability("pressMenuAtUnlock"))
                );
                this.driver = new AndroidDriver(DriverProvider.getServiceUrl(), desiredCapabilities);
                break;
        }
    }

    public AppiumDriver getDriver(DesiredCapabilities desiredCapabilities) {
        if (driver == null) {
            createWebDriver(desiredCapabilities);
        }
        return driver;
    }
}
