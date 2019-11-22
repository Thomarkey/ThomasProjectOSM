package be.refleqt.appium.driver.setup.PropertiesLoaders;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

public class CapabilitiesLoader {

    private static ThreadLocal<ResourceBundle> deviceCapabilities = new ThreadLocal<>();

    private CapabilitiesLoader() {
    }

    public static DesiredCapabilities getCapabilities(String device) {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        DesiredCapabilities defaultCapabilities = new DesiredCapabilities();

        deviceCapabilities.set(ResourceBundle.getBundle("properties/devices", new Locale(device)));
        deviceCapabilities.get().keySet().forEach(k -> desiredCapabilities.setCapability(k, deviceCapabilities.get().getString(k)));

        if (desiredCapabilities.getPlatform() == null) {
            throw new RuntimeException("applicationName is not set or is an invalid value. Only ANDROID or IOS is allowed." +
                    " Check your device capabilities in the resource bundle!");
        }

        deviceCapabilities.set(ResourceBundle.getBundle(
                "properties/devices",
                new Locale("DEFAULT_" + desiredCapabilities.getPlatform().name())
        ));

        deviceCapabilities.get().keySet().forEach(k -> defaultCapabilities.setCapability(k, deviceCapabilities.get().getString(k)));

        defaultCapabilities.merge(desiredCapabilities);

        if (defaultCapabilities.getPlatform().equals(Platform.IOS)) {
            File f = new File(System.getProperty("user.home") + "/Library/Developer/Xcode/DerivedData");

            File[] matchingFiles = f.listFiles((dir, name) -> name.startsWith("WebDriverAgent"));

            for (File file : matchingFiles) {
                defaultCapabilities.merge(
                        setPreBuiltWDA(file,
                                defaultCapabilities
                                        .getCapability("realDevice")
                                        .toString()
                                        .equalsIgnoreCase("true"))
                );
            }
        }

        return defaultCapabilities;
    }

    private static DesiredCapabilities setPreBuiltWDA(File file, boolean realDevice) {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

        String termToContain = realDevice ? "Debug-iphoneos" : "Debug-iphonesimulator";

        if (StringUtils.containsIgnoreCase(
                search(file, "WebDriverAgentRunner-Runner.app").getAbsolutePath(),
                termToContain)
        ) {
            desiredCapabilities.setCapability("usePrebuiltWDA", true);
            desiredCapabilities.setCapability("derivedDataPath", file.getAbsolutePath());
        }

        return desiredCapabilities;
    }

    private static ThreadLocal<File> foundFile = new ThreadLocal<>();

    private static File search(File file, String fileNameToSearch) {
        if (file.isDirectory()) {
            for (File temp : file.listFiles()) {
                if (fileNameToSearch.equalsIgnoreCase(temp.getName())) {
                    foundFile.set(temp);
                }

                if (temp.isDirectory()) {
                    search(temp, fileNameToSearch);
                }
            }
        }
        return foundFile.get();
    }
}
