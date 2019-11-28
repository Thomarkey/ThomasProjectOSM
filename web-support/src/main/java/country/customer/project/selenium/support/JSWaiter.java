package country.customer.project.selenium.support;

import country.customer.project.selenium.driver.DriverProvider;
import java.util.concurrent.TimeUnit;
import org.awaitility.Awaitility;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class JSWaiter {

    private JSWaiter() {
    }

    //Wait for Angular Load
    private static void waitForAngularLoad(WebDriver jsWaitDriver) {
        JavascriptExecutor jsExec = (JavascriptExecutor) jsWaitDriver;

        String angularReadyScript = "return angular.element(document).injector().get('$http').pendingRequests.length === 0";

        //Get Angular is Ready
        boolean angularReady = Boolean.parseBoolean(jsExec.executeScript(angularReadyScript).toString());

        //Wait ANGULAR until it is Ready!
        if (!angularReady) {
            //Wait for Angular to load
            Awaitility.await()
                    .ignoreExceptions()
                    .atMost(10, TimeUnit.SECONDS)
                    .until(() -> Boolean.valueOf(jsExec.executeScript(angularReadyScript).toString()));
        }
    }

    //Wait Until JS Ready
    private static void waitUntilJSReady(WebDriver jsWaitDriver) {
        JavascriptExecutor jsExec = (JavascriptExecutor) jsWaitDriver;

        //Get JS is Ready
        boolean jsReady = jsExec.executeScript("return document.readyState").toString().equals("complete");

        //Wait Javascript until it is Ready!
        if (!jsReady) {
            //Wait for Javascript to load
            Awaitility.await()
                    .ignoreExceptions()
                    .atMost(10, TimeUnit.SECONDS)
                    .until(() -> jsExec.executeScript("return document.readyState").toString().equals("complete"));
        }
    }

    //Wait Until Angular and JS Ready
    public static void waitUntilAngularReady() {
        WebDriver jsWaitDriver = DriverProvider.getDriver();
        JavascriptExecutor jsExec = (JavascriptExecutor) jsWaitDriver;

        //First check that ANGULAR is defined on the page. If it is, then wait ANGULAR
        Boolean angularUnDefined = (Boolean) jsExec.executeScript("return window.angular === undefined");
        if (!angularUnDefined) {
            Boolean angularInjectorUnDefined = (Boolean) jsExec.executeScript("return angular.element(document).injector() === undefined");
            if (!angularInjectorUnDefined) {
                //Wait Angular Load
                waitForAngularLoad(jsWaitDriver);

                //Wait JS Load
                waitUntilJSReady(jsWaitDriver);
            }
        }
    }
}
