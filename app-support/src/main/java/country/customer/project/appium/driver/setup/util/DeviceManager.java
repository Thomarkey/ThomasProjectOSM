package country.customer.project.appium.driver.setup.util;

import country.customer.project.appium.driver.DriverProvider;
import io.appium.java_client.android.AndroidDriver;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

public class DeviceManager {

    public static final int TIMEOUT = 1;

    public static void unLockAndroidDevice(String id, Boolean menuBtn) {
        String line;
        boolean screenOn = false;

        BufferedReader buf = new BufferedReader(
                new InputStreamReader(
                        executeCommand("Checking if screen is on: " + id,
                                "adb", "-s", id, "shell", "service", "call", "power", "12")
                                .getInputStream()));
        try {
            while ((line = buf.readLine()) != null) {
                screenOn = line.contains("1");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (screenOn) {
            executeCommand("Keypress to keep screen active on: " + id, "adb", "-s", id, "shell",
                    "input", "keyevent", "1");
        } else {
            executeCommand("Pressed power button on: " + id, "adb", "-s", id, "shell",
                    "input", "keyevent", "26");
            if (menuBtn) {
                executeCommand("Pressed menu button on: " + id, "adb", "-s", id, "shell",
                        "input", "keyevent", "82");
            }
        }
    }

    public static void installApp(DesiredCapabilities desiredCapabilities) {
        File f = new File("src/test/resources");
        String id = desiredCapabilities.getCapability("udid").toString();

        File[] matchingFiles = f.listFiles((dir, name) -> {
            if (desiredCapabilities.getPlatform().is(Platform.ANDROID)) {
                return name.endsWith("apk");
            } else {
                return name.endsWith("ipa");
            }
        });


        if (matchingFiles == null) {
            return;
        }

        for (File file : matchingFiles) {
            if (file.getName().contains("apk")) {
                executeCommand("Installing " + file.getName() + " on: " + id, "adb", "-s", id,
                        "install", "-r", "-d", file.getAbsolutePath());
            } else {
                if (id != null && id.equalsIgnoreCase("auto")) {
                    id = getiOSUdId(desiredCapabilities);
                }

                if (desiredCapabilities.getCapability("bundleId") != null) {
                    executeCommand("Uninstalling app on: " + id, "ideviceinstaller", "-u", id,
                            "-U", desiredCapabilities.getCapability("bundleId").toString());
                }
                executeCommand("Installing app on: " + id, "ideviceinstaller", "-u", id, "-i",
                        file.getAbsolutePath());
            }
        }
    }


    private static Process executeCommand(String message, String... command) {
        try {
            System.out.println(message);
            ProcessBuilder pb = new ProcessBuilder(command);
            Process pc = pb.start();
            pc.waitFor(TIMEOUT, TimeUnit.MINUTES);
            return pc;
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getiOSUdId(DesiredCapabilities desiredCapabilities) {
        String device = desiredCapabilities.getCapability("deviceName").toString();
        String version = desiredCapabilities.getCapability("platformVersion").toString();

        BufferedReader buf = new BufferedReader(
                new InputStreamReader(
                        executeCommand("Finding id of: " + device,
                                "instruments", "-s", "devices").getInputStream()
                ));

        List<String> lines = new ArrayList<>();
        String line;

        try {
            while ((line = buf.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String row : lines) {
            if (row.contains("[")) {
                if (row.substring(0, row.indexOf("[")).contains(version)) {
                    return row.substring(row.indexOf("[") + 1, row.indexOf("]"));
                }
            }
        }

        return null;
    }

    public static void lockAndroidDevice() {
        if (DriverProvider.getDriver().getCapabilities().getPlatform().is(Platform.ANDROID)) {
            ((AndroidDriver) DriverProvider.getDriver()).lockDevice();
        }
    }
}
