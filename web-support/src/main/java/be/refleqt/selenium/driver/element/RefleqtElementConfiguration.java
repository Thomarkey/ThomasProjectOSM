package be.refleqt.selenium.driver.element;

import be.refleqt.selenium.support.JSWaiter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import net.jodah.failsafe.RetryPolicy;
import org.openqa.selenium.*;

public class RefleqtElementConfiguration {

    /****************
     Time Outs + Setters
     *****************/
    public static long timeOut = 10;
    public static long shortTimeOut = 5;
    public static long longTimeOut = 20;

    public static void setTimeOut(long timeOut) {
        RefleqtElementConfiguration.timeOut = timeOut;
    }

    public static void setShortTimeOut(long shortTimeOut) {
        RefleqtElementConfiguration.shortTimeOut = shortTimeOut;
    }

    public static void setLongTimeOut(long longTimeOut) {
        RefleqtElementConfiguration.longTimeOut = longTimeOut;
    }

    /****************
     Retry declaration
     *****************/
    public RetryPolicy<Object> retryPolicy = new RetryPolicy<>()
            .handle(getExceptionClasses())
            .withDelay(Duration.ofSeconds(2))
            .onFailedAttempt(e -> System.out.println("An error was thrown, but retrying: " + e.getLastFailure()))
            .withMaxRetries(3);

    private List<Class<? extends Throwable>> getExceptionClasses() {
        List<Class<? extends Throwable>> list = new ArrayList<>();
        list.add(ElementClickInterceptedException.class);
        list.add(StaleElementReferenceException.class);
        list.add(WebDriverException.class);
        return list;
    }

    /****************
     Generic waitFor
     *****************/
    protected void waitForAngularLoad() {
        JSWaiter.waitUntilAngularReady();
    }
}
