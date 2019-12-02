package country.customer.project.appium.driver.setup.DriverManagers;

import country.customer.project.appium.driver.setup.PropertiesLoaders.EnvironmentPropertiesLoader;
import country.customer.project.appium.driver.setup.enums.EnvironmentType;
import country.customer.project.appium.support.ScenarioManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.cucumber.datatable.dependency.com.fasterxml.jackson.databind.JsonNode;
import io.cucumber.datatable.dependency.com.fasterxml.jackson.databind.ObjectMapper;
import static io.restassured.RestAssured.given;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

public class RemoteDriverManager extends DriverManager {

    public RemoteDriverManager() {
    }

    @Override
    public AppiumDriver getDriver(DesiredCapabilities desiredCapabilities) {
        if (driver == null) {
            createWebDriver(desiredCapabilities);
        }
        return driver;
    }

    @Override
    public void createWebDriver(DesiredCapabilities desiredCapabilities) {
        String environment = EnvironmentType.fromString(desiredCapabilities.getCapability("env")).toString();

        String url = String.format(
                EnvironmentPropertiesLoader.getInstance(desiredCapabilities).getProperty(environment + "_url"),
                EnvironmentPropertiesLoader.getInstance(desiredCapabilities).getProperty(environment + "_username"),
                EnvironmentPropertiesLoader.getInstance(desiredCapabilities).getProperty(environment + "_accessKey")
        );

        DesiredCapabilities uploadToCloud = uploadToCloud(desiredCapabilities);

        uploadToCloud.setCapability(
                "name",
                "Scenario name: " + ScenarioManager.getScenario().getName()
        );

        try {
            switch (desiredCapabilities.getPlatform()) {
                case IOS:
                    this.driver = new IOSDriver(new URL(url), uploadToCloud);
                    break;
                case ANDROID:
                    this.driver = new AndroidDriver(new URL(url), uploadToCloud);
                    break;
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Something is wrong with the given url.");
        }
    }

    private String androidBS, iOSBS, androidSL, iOSSL;

    private DesiredCapabilities uploadToCloud(DesiredCapabilities desiredCapabilities) {
        File f = new File("src/test/resources");
        boolean isAndroid = desiredCapabilities.getPlatform().is(Platform.ANDROID);

        File[] matchingFiles = f.listFiles((dir, name) -> {
            if (isAndroid) {
                return name.endsWith("apk");
            } else {
                return name.endsWith("ipa");
            }
        });

        if (matchingFiles == null) {
            throw new RuntimeException("Nothing found to upload! The cloud provider needs the app to install!");
        }

        switch (EnvironmentType.fromString(desiredCapabilities.getCapability("env"))) {
            case BROWSERSTACK:
                synchronized (RemoteDriverManager.class) {
                    if (isAndroid) {
                        if (androidBS == null) {
                            uploadToBrowserStack(matchingFiles[0], isAndroid);
                        }
                        desiredCapabilities.setCapability("app", androidBS);
                    } else {
                        if (iOSBS == null) {
                            uploadToBrowserStack(matchingFiles[0], isAndroid);
                        }
                        desiredCapabilities.setCapability("app", iOSBS);
                    }
                }
                break;
            case SAUCELABS:
                synchronized (RemoteDriverManager.class) {
                    if (isAndroid) {
                        if (androidSL == null) {
                            uploadToSauceLabs(matchingFiles[0], isAndroid);
                        }
                        desiredCapabilities.setCapability("testobject_app_id", androidSL);
                    } else {
                        if (iOSSL == null) {
                            uploadToSauceLabs(matchingFiles[0], isAndroid);
                        }
                        desiredCapabilities.setCapability("testobject_app_id", iOSSL);
                    }
                    desiredCapabilities.setCapability("testobject_api_key", System.getProperty("saucelabs_accessKey"));
                }
                break;
            default:
                System.out.println("No upload happened.");
        }

        return desiredCapabilities;
    }

    private void uploadToSauceLabs(File fileToUpload, boolean isAndroid) {
        try {
            JsonNode body = getBodyNode(
                    given()
                            .auth()
                            .preemptive()
                            .basic(System.getProperty("saucelabs_username"), System.getProperty("saucelabs_accessKey"))
                            .body(fileToUpload)
                            .request("POST", new URL("https://app.testobject.com:443/api/storage/upload"))
                            .getBody().print()
            );

            if (body == null) {
                throw new RuntimeException("Upload failed to SauceLabs!");
            }

            if (!body.isNumber()) {
                throw new RuntimeException("Upload failed to SauceLabs!");
            }

            if (isAndroid) {
                androidSL = body.toString();
            } else {
                iOSSL = body.toString();
            }
        } catch (MalformedURLException ignored) {
            throw new RuntimeException("Upload failed to SauceLabs!");
        }
    }

    private void uploadToBrowserStack(File fileToUpload, boolean isAndroid) {
        try {
            JsonNode body = getBodyNode(
                    given()
                            .auth()
                            .preemptive()
                            .basic(System.getProperty("browserstack_username"), System.getProperty("browserstack_accessKey"))
                            .multiPart("file", fileToUpload)
                            .request("POST", new URL("https://api-cloud.browserstack.com/app-automate/upload"))
                            .getBody().print()
            );

            if (body == null) {
                throw new RuntimeException("Upload failed to BrowserStack!");
            }

            if (isAndroid) {
                androidBS = body.get("app_url").asText();
            } else {
                iOSBS = body.get("app_url").asText();
            }
        } catch (MalformedURLException ignored) {
            throw new RuntimeException("Upload failed to BrowserStack!");
        }
    }

    private JsonNode getBodyNode(String body) {
        try {
            return new ObjectMapper().readTree(body);
        } catch (IOException e) {
            //If this is thrown it means there is no API response.
            return null;
        }
    }
}
