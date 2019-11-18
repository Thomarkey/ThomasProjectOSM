package be.refleqt.selenium.support;

import java.util.*;
import org.testng.annotations.*;

public class CommonDataProvider {

    @DataProvider(name = "getBrowsers", parallel = true)
    public static Object[][] getDevices() {

        List<Object[]> array = new ArrayList<>();
        String browsers = System.getProperty("browser", "chrome");

        for (String browser : browsers.split(",")) {
            array.add(new Object[]{browser});
        }

        return array.toArray(new Object[][]{});
    }
}
