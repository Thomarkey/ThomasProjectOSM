package be.refleqt.appium.driver;

import be.refleqt.appium.support.ScenarioManager;
import io.appium.java_client.events.api.general.AppiumWebDriverEventListener;
import org.openqa.selenium.*;

public class CustomAppiumEventDriverListener implements AppiumWebDriverEventListener {

    @Override
    public void beforeAlertAccept(WebDriver webDriver) {
        //No screenshot needed at this step.
    }

    @Override
    public void afterAlertAccept(WebDriver webDriver) {
        //No screenshot needed at this step.
    }

    @Override
    public void afterAlertDismiss(WebDriver webDriver) {
        //No screenshot needed at this step.
    }

    @Override
    public void beforeAlertDismiss(WebDriver webDriver) {
        //No screenshot needed at this step.
    }

    @Override
    public void beforeNavigateTo(String s, WebDriver webDriver) {
        ScenarioManager.saveScreenshot();
    }

    @Override
    public void afterNavigateTo(String s, WebDriver webDriver) {
        //No screenshot needed at this step.
    }

    @Override
    public void beforeNavigateBack(WebDriver webDriver) {
        ScenarioManager.saveScreenshot();
    }

    @Override
    public void afterNavigateBack(WebDriver webDriver) {
        //No screenshot needed at this step.
    }

    @Override
    public void beforeNavigateForward(WebDriver webDriver) {
        ScenarioManager.saveScreenshot();
    }

    @Override
    public void afterNavigateForward(WebDriver webDriver) {
        //No screenshot needed at this step.
    }

    @Override
    public void beforeNavigateRefresh(WebDriver webDriver) {
        ScenarioManager.saveScreenshot();
    }

    @Override
    public void afterNavigateRefresh(WebDriver webDriver) {
        //No screenshot needed at this step.
    }

    @Override
    public void beforeFindBy(By by, WebElement webElement, WebDriver webDriver) {
        //No screenshot needed at this step.
    }

    @Override
    public void afterFindBy(By by, WebElement webElement, WebDriver webDriver) {
        //No screenshot needed at this step.
    }

    @Override
    public void beforeClickOn(WebElement webElement, WebDriver webDriver) {
        ScenarioManager.saveScreenshot();
    }

    @Override
    public void afterClickOn(WebElement webElement, WebDriver webDriver) {
        //No screenshot needed at this step.
    }

    @Override
    public void beforeChangeValueOf(WebElement webElement, WebDriver webDriver, CharSequence[] charSequences) {
        //No screenshot needed at this step.
    }

    @Override
    public void afterChangeValueOf(WebElement webElement, WebDriver webDriver, CharSequence[] charSequences) {
        //No screenshot needed at this step.
    }

    @Override
    public void beforeChangeValueOf(WebElement webElement, WebDriver webDriver) {
        ScenarioManager.saveScreenshot();
    }

    @Override
    public void afterChangeValueOf(WebElement webElement, WebDriver webDriver) {
        //No screenshot needed at this step.
    }

    @Override
    public void beforeScript(String s, WebDriver webDriver) {
        ScenarioManager.saveScreenshot();
    }

    @Override
    public void afterScript(String s, WebDriver webDriver) {
        //No screenshot needed at this step.
    }

    @Override
    public void beforeSwitchToWindow(String s, WebDriver webDriver) {
        //No screenshot needed at this step.
    }

    @Override
    public void afterSwitchToWindow(String s, WebDriver webDriver) {
        //No screenshot needed at this step.
    }

    @Override
    public void onException(Throwable throwable, WebDriver webDriver) {
        //No screenshot needed at this step.
    }

    @Override
    public <X> void beforeGetScreenshotAs(OutputType<X> outputType) {
        //No screenshot needed at this step.
    }

    @Override
    public <X> void afterGetScreenshotAs(OutputType<X> outputType, X x) {
        //No screenshot needed at this step.
    }

    @Override
    public void beforeGetText(WebElement webElement, WebDriver webDriver) {
        //No screenshot needed at this step.
    }

    @Override
    public void afterGetText(WebElement webElement, WebDriver webDriver, String s) {
        //No screenshot needed at this step.
    }
}
