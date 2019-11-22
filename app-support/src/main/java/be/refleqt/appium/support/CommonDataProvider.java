package be.refleqt.appium.support;

import java.util.ArrayList;
import java.util.List;
import org.testng.annotations.DataProvider;

public class CommonDataProvider {

    @DataProvider(name = "getDevices", parallel = true)
    public static Object[][] getDevices() {

        List<Object[]> array = new ArrayList<>();
        String devices = System.getProperty("devices", "EMULATOR_ANDROID");

        for (String device : devices.split(",")) {
            array.add(new Object[]{device});
        }

        return array.toArray(new Object[][]{});
    }
}
