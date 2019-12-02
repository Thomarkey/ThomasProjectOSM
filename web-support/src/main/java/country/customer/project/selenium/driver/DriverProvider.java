package country.customer.project.selenium.driver;

import country.customer.project.selenium.driver.setup.DriverManagerFactory;
import country.customer.project.selenium.driver.setup.enums.BrowserType;
import country.customer.project.selenium.driver.setup.enums.EnvironmentType;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.*;
import javax.imageio.ImageIO;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;

public class DriverProvider {

    private DriverProvider() {
    }

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<EventFiringWebDriver> eventHandlerDriver = new ThreadLocal<>();
    private static EnvironmentType environmentType = EnvironmentType.fromString(System.getProperty("env", "local"));
    private static List<HashMap<String, String>> browserSetup = new ArrayList<>();

    public static void setBrowser(String browser) {
        synchronized (DriverProvider.class) {
            HashMap<String, String> newBrowserSetup = new HashMap<>();
            newBrowserSetup.put("browser", browser);
            browserSetup.add(newBrowserSetup);
        }
    }

    private static HashMap<String, String> getDefaultBrowser() {
        HashMap<String, String> defaultBrowserSetup = new HashMap<>();
        defaultBrowserSetup.put("browser", System.getProperty("browser", "chrome"));
        return defaultBrowserSetup;
    }

    private static void setPoolIfNecessary() {
        synchronized (DriverProvider.class) {
            if (browserSetup != null) {
                if (browserSetup.stream().anyMatch(b -> b.get("runner") == null)) {

                    String threadName = Thread.currentThread().getName()
                            .replaceAll("(\\d+).+", "$1");

                    if (browserSetup.stream()
                            .filter(b -> b.get("runner") != null)
                            .noneMatch(b -> b.get("runner")
                                    .equalsIgnoreCase(threadName))) {

                        browserSetup
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

    public static BrowserType getBrowser() {
        setPoolIfNecessary();

        return BrowserType.fromString(
                browserSetup
                        .stream()
                        .filter(b -> Thread.currentThread().getName().contains(b.get("runner")))
                        .findFirst()
                        .orElse(getDefaultBrowser())
                        .get("browser")
        );
    }

    public static void setUpDriver() {
        setUpDriver(new DesiredCapabilities());
    }

    public static void setUpDriver(DesiredCapabilities desiredCapabilities) {
        driver.set(
                DriverManagerFactory.getDriver(
                        getBrowser(),
                        environmentType,
                        desiredCapabilities
                ));

        driver.get().manage().window().setSize(new Dimension(1920, 1080));

        eventHandlerDriver.set(new EventFiringWebDriver(driver.get()));
        eventHandlerDriver.get().register(new CustomEventDriverListener());
    }

    public static EventFiringWebDriver getDriver() {
        return eventHandlerDriver.get();
    }

    public static void goToURL(String url) {
        eventHandlerDriver.get().get(url);
    }

    public static void tearDownDriver() {
        if (driver.get() != null) {
            driver.get().quit();
        }
        if (eventHandlerDriver.get() != null) {
            eventHandlerDriver.get().quit();
        }
    }

    private static byte[] getScreenshot() {
        if (shouldEnableScreenshot()) {
            return ((TakesScreenshot) driver.get()).getScreenshotAs(OutputType.BYTES);
        } else {
            return null;
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
        switch (environmentType) {
            case DOCKER:
            case LOCAL:
                return System.getProperty("takeScreenshots", "true")
                        .equalsIgnoreCase("true");
            case SAUCELABS:
            case BROWSERSTACK:
                return System.getProperty("takeScreenshots", "false")
                        .equalsIgnoreCase("true");
            default:
                return true;
        }
    }
}
