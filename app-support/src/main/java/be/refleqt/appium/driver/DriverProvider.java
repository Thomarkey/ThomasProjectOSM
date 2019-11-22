package be.refleqt.appium.driver;

import be.refleqt.appium.driver.setup.DriverManagerFactory;
import be.refleqt.appium.driver.setup.PropertiesLoaders.CapabilitiesLoader;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.events.EventFiringWebDriverFactory;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.List;
import java.util.*;
import javax.imageio.ImageIO;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;

public class DriverProvider {

    private DriverProvider() {
    }

    private static ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<DesiredCapabilities> desiredCapabilities = new ThreadLocal<>();
    private static List<HashMap<String, String>> deviceSetup = new ArrayList<>();
    private static AppiumDriverLocalService service;

    public static void setDevice(String device) {
        synchronized (DriverProvider.class) {
            HashMap<String, String> newDeviceSetup = new HashMap<>();
            newDeviceSetup.put("device", device);
            deviceSetup.add(newDeviceSetup);
        }
    }

    private static HashMap<String, String> getDefaultDevice() {
        HashMap<String, String> defaultDeviceSetup = new HashMap<>();
        defaultDeviceSetup.put("device", System.getProperty("device", "emulator_android"));
        return defaultDeviceSetup;
    }

    private static void setPoolIfNecessary() {
        synchronized (DriverProvider.class) {
            if (deviceSetup != null) {
                if (deviceSetup.stream().anyMatch(b -> b.get("runner") == null)) {

                    String threadName = Thread.currentThread().getName()
                            .replaceAll("(\\d+).+", "$1");

                    if (deviceSetup.stream()
                            .filter(b -> b.get("runner") != null)
                            .noneMatch(b -> b.get("runner")
                                    .equalsIgnoreCase(threadName))) {

                        deviceSetup
                                .stream()
                                .filter(b -> b.get("runner") == null)
                                .findAny()
                                .orElseThrow(() -> new RuntimeException("This will get catched"))
                                .put("runner", threadName);
                    }
                }
            }
        }
    }

    public static String getDevice() {
        setPoolIfNecessary();

        return deviceSetup
                .stream()
                .filter(b -> Thread.currentThread().getName().contains(b.get("runner")))
                .findFirst()
                .orElse(getDefaultDevice())
                .get("device");
    }

    public static void setUpDriver() {
        desiredCapabilities.set(CapabilitiesLoader.getCapabilities(getDevice()));

        driver.set(
                EventFiringWebDriverFactory.getEventFiringWebDriver(
                        DriverManagerFactory.getDriver(
                                desiredCapabilities.get()
                        ),
                        new CustomAppiumEventDriverListener()
                )
        );
    }

    private static byte[] getScreenshot() {
        if (shouldEnableScreenshot()) {
            return ((TakesScreenshot) driver.get()).getScreenshotAs(OutputType.BYTES);
        } else {
            return null;
        }
    }

    public static AppiumDriver getDriver() {
        return driver.get();
    }

    public static void tearDownDriver() {
        if (driver.get() != null) {
            driver.get().quit();
        }
    }

    public static byte[] getScaledScreenshot(int scaleFactor) {
        try {
            byte[] screenshot = DriverProvider.getScreenshot();
            ByteArrayInputStream in = new ByteArrayInputStream(screenshot);
            BufferedImage img = ImageIO.read(in);

            int height = img.getHeight() / scaleFactor;
            int width = img.getWidth() / scaleFactor;

            Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            imageBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(0, 0, 0), null);

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            ImageIO.write(imageBuff, "jpg", buffer);
            screenshot = buffer.toByteArray();

            return screenshot;
        } catch (Exception e) {
            return null;
        }
    }

    private static boolean shouldEnableScreenshot() {
        return Boolean.parseBoolean(
                String.valueOf(desiredCapabilities.get().getCapability("takeScreenshot"))
        );
    }

    public static void startAppiumServer() {
        AppiumServiceBuilder builder;
        builder = new AppiumServiceBuilder();
        builder.withArgument(GeneralServerFlag.LOG_LEVEL, "info:warn");
        builder.withIPAddress("127.0.0.1");
        builder.usingAnyFreePort();

        service = AppiumDriverLocalService.buildService(builder);
        service.start();
    }

    public static void stopAppiumServer() {
        service.stop();
    }

    public static URL getServiceUrl() {
        return service.getUrl();
    }
}