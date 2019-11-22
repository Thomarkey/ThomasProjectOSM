package be.refleqt.appium.driver.setup.DriverManagers;

import be.refleqt.appium.driver.DriverProvider;
import be.refleqt.appium.driver.setup.util.DeviceManager;
import be.refleqt.appium.support.GenericDataHelper;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

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
