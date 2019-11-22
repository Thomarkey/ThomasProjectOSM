package be.refleqt.appium.support;

import be.refleqt.appium.driver.DriverProvider;
import io.appium.java_client.*;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.touch.WaitOptions;
import static io.appium.java_client.touch.offset.PointOption.point;
import java.util.NoSuchElementException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;
import org.apache.commons.lang3.StringUtils;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import org.awaitility.Duration;
import org.awaitility.core.ConditionTimeoutException;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;

public abstract class GenericAbstractPage {
    protected final AppiumDriver driver;

    public Duration timeOut = new Duration(20, TimeUnit.SECONDS);
    public Duration shortTimeOut = new Duration(6, TimeUnit.SECONDS);
    public Duration longTimeOut = new Duration(60, TimeUnit.SECONDS);
    public final boolean isAndroid;

    public RetryPolicy<Object> retryPolicy = new RetryPolicy<>()
            .handle(getExceptionClasses())
            .withDelay(java.time.Duration.ofSeconds(2))
            .onFailedAttempt(e -> System.out.println("An error was thrown, but retrying: " + e.getLastFailure()))
            .withMaxRetries(3);

    private List<Class<? extends Throwable>> getExceptionClasses() {
        List<Class<? extends Throwable>> list = new ArrayList<>();
        list.add(ElementClickInterceptedException.class);
        list.add(StaleElementReferenceException.class);
        list.add(WebDriverException.class);
        return list;
    }


    public GenericAbstractPage() {
        this.driver = DriverProvider.getDriver();
        isAndroid = driver.getCapabilities().getPlatform().is(Platform.ANDROID);
        PageFactory.initElements(new AppiumFieldDecorator(driver, java.time.Duration.ofMillis(100)), this);
    }


    protected void clickOnElement(MobileElement element) {
        Failsafe.with(retryPolicy).run(() -> {
            waitForVisibility(element);
            element.click();
        });
    }

    protected void sendKeysToElement(MobileElement element, String keysToSend) {
        Failsafe.with(retryPolicy).run(() -> {
            waitForVisibility(element);
            element.sendKeys(keysToSend);
        });
    }

    // Wait methods
    protected void waitForVisibility(MobileElement element) {
        waitForVisibility(element, timeOut);
    }

    protected void waitShortForVisibility(MobileElement element) {
        waitForVisibility(element, shortTimeOut);
    }

    private void waitForVisibility(MobileElement element, Duration timeOut) {
        await()
                .ignoreExceptions()
                .atMost(timeOut)
                .until(() -> isPresent(element));
    }

    /**
     * waits for FIRST element to be visible, while ignoring @IndexOutOfBoundsException
     *
     * @param listOfElements list that contains the element that needs to be visible
     */
    protected <T> void waitForVisibility(List<T> listOfElements) {
        waitForVisibility(listOfElements, 0);
    }

    /**
     * waits for FIRST element to be visible, while ignoring @IndexOutOfBoundsException
     *
     * @param listOfElements list that contains the element that needs to be visible
     */
    protected void waitForVisibilityWithSwipe(List<MobileElement> listOfElements) {
        await()
                .atMost(timeOut)
                .ignoreExceptions()
                .until(() -> {
                    swipeDownWithoutExtraTap();
                    return listOfElements.size() > 0;
                });
    }

    /**
     * waits for element with INDEX to be visible, while ignoring @IndexOutOfBoundsException
     *
     * @param listOfElements listOfElements list that contains the element that needs to be visible
     * @param index          index of the element that needs to be visible
     */
    protected <T> void waitForVisibility(List<T> listOfElements, int index) {
        await()
                .atMost(timeOut)
                .ignoreExceptions()
                .until(() -> listOfElements.size() > index);
    }

    protected void waitTextToContain(MobileElement element, String text) {
        await()
                .atMost(timeOut)
                .ignoreExceptions()
                .until(() -> StringUtils.containsIgnoreCase(element.getText(), text));
    }

    protected void waitForClickable(MobileElement element) {
        waitForClickable(element, timeOut);
    }


    protected void waitForShortClickable(MobileElement element) {
        waitForClickable(element, shortTimeOut);
    }

    protected void waitForClickable(MobileElement element, Duration timeOut) {
        await()
                .atMost(timeOut)
                .ignoreExceptions()
                .until(element::isEnabled);
    }


    protected void waitForAlertToBePresent() {
        await()
                .atMost(shortTimeOut)
                .ignoreExceptions()
                .until(() -> driver.switchTo().alert() != null);
    }

    protected Boolean isAlertPresent() {
        try {
            waitForAlertToBePresent();
            return true;
        } catch (ConditionTimeoutException e) {
            return false;
        }
    }

    protected Boolean isPresent(MobileElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    protected Boolean isPresent(List<MobileElement> elementList) {
        try {
            waitForVisibility(elementList);
            return true;
        } catch (ConditionTimeoutException e) {
            return false;
        }
    }

    protected Boolean isPresentWait(MobileElement element) {
        try {
            waitShortForVisibility(element);
            return element.isDisplayed();
        } catch (NoSuchElementException | ConditionTimeoutException e) {
            return false;
        }
    }

    protected Boolean isEnabledWait(MobileElement element) {
        try {
            waitForShortClickable(element);
            return element.isEnabled();
        } catch (NoSuchElementException | ConditionTimeoutException e) {
            return false;
        }
    }

    protected List<MobileElement> findElementsByID(String id) {
        return ((WebDriver) driver).findElements(By.id(id));
    }


    // Scroll methods
    protected void swipeDown() {
        Dimension size = swipe(0.30, 0.70, 0.30);

        tapCoordinates(size, 0.3, 0.01);
    }

    private Dimension swipe(double v, double v2, double v3) {
        Dimension size = driver.manage().window().getSize();

        int anchor = (int) (size.width * v);
        int endPoint = (int) (size.height * v2);
        int startPoint = (int) (size.height * v3);

        (new TouchAction(driver))
                .press(point(anchor, startPoint))
                .waitAction(WaitOptions.waitOptions(java.time.Duration.ofMillis(1000)))
                .moveTo(point(anchor, endPoint))
                .release()
                .perform();

        return size;
    }

    protected void swipeDownWithoutExtraTap() {
        Dimension size = swipe(0.30, 0.70, 0.30);
    }

    protected void swipeDown(MobileElement element) {
        Dimension size = element.getSize();

        swipe(
                (int) (element.getLocation().getX() + (size.width * 0.5)),
                (int) (element.getLocation().getY() + (size.height * 0.1)),
                (int) (element.getLocation().getY() + (size.height * 0.9))
        );
    }

    protected void swipeUp() {
        Dimension size = swipe(0.20, 0.20, 0.80);

        tapCoordinates(size, 0.3, 0.01);
    }

    protected void swipeUpWithoutExtraTap() {
        swipe(0.20, 0.20, 0.80);
    }

    protected void swipeUp(MobileElement element) {
        Dimension size = element.getSize();
        swipe(
                (int) (element.getLocation().getX() + (size.width * 0.5)),
                (int) (element.getLocation().getY() + (size.height * 0.9)),
                (int) (element.getLocation().getY() + (size.height * 0.1))
        );
    }

    protected void swipeHorizontalLeft(MobileElement element) {
        swipeHorizontally(element.getSize(), 0.9, 0.1);
    }

    protected void swipeHorizontalLeft(Dimension size) {
        swipeHorizontally(size, 0.9, 0.1);
    }

    protected void swipeHorizontalRight(MobileElement element) {
        swipeHorizontally(element.getSize(), 0.1, 0.9);
    }

    protected void swipeHorizontalRight(Dimension size) {
        swipeHorizontally(size, 0.1, 0.9);
    }

    private void swipeHorizontally(Dimension size, double v, double v2) {
        int anchor = (int) (size.height * 0.5);
        int startPoint = (int) (size.width * v);
        int endPoint = (int) (size.width * v2);

        new TouchAction(driver)
                .press(point(startPoint, anchor))
                .moveTo(point(endPoint, anchor))
                .release()
                .perform();
    }

    protected void tapCoordinates(Dimension size, Double heightMultiplier, Double widthMultiplier) {
        int y = (int) (size.height * heightMultiplier);
        int x = (int) (size.width * widthMultiplier);

        new TouchAction(driver).tap(point(x, y)).perform();
    }

    protected void hideKeyboardWithClick() {
        tapCoordinates(driver.manage().window().getSize(), 0.3, 0.01);
    }


    protected void sleep(long milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            ScenarioManager.writeLine(e.toString());
        }
    }

    protected void validateCurrentUrl(String urlToCheck) {
        assertThat(driver.getCurrentUrl()).isEqualToIgnoringCase(urlToCheck);
    }

    protected void validatePresence(MobileElement element, boolean presence) {
        assertThat(isPresentWait(element)).isEqualTo(presence);
    }

    protected void validatePresence(MobileElement element) {
        validatePresence(element, true);
    }

    protected void validateTextToContain(MobileElement element, String toContain) {
        waitForVisibility(element);
        assertThat(element.getText()).containsIgnoringCase(toContain);
    }

    protected void validateTextToMatch(MobileElement element, String toMatch) {
        waitForVisibility(element);
        assertThat(element.getText()).isEqualToIgnoringCase(toMatch);
    }

    protected void validateAttribute(MobileElement element, String attribute, String haveToHave) {
        waitForVisibility(element);
        assertThat(element.getAttribute(attribute)).isEqualToIgnoringCase(haveToHave);
    }

    protected void validateListAnyToMatch(List<MobileElement> mobileElementList, String termToContain) {
        waitForVisibility(mobileElementList);
        assertThat(
                mobileElementList
                        .stream()
                        .anyMatch(element ->
                                StringUtils.containsIgnoreCase(element.getText(), termToContain))
        ).isTrue();
    }

    protected void validateListAllToMatch(List<MobileElement> mobileElementList, String termToContain) {
        waitForVisibility(mobileElementList);
        assertThat(
                mobileElementList
                        .stream()
                        .allMatch(element ->
                                StringUtils.containsIgnoreCase(element.getText(), termToContain))
        ).isTrue();
    }

    protected void validateListNoneToMatch(List<MobileElement> mobileElementList, String termToContain) {
        waitForVisibility(mobileElementList);
        assertThat(
                mobileElementList
                        .stream()
                        .noneMatch(element ->
                                StringUtils.containsIgnoreCase(element.getText(), termToContain))
        ).isTrue();
    }
}
