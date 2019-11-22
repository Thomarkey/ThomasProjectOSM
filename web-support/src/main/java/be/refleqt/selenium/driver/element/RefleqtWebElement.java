package be.refleqt.selenium.driver.element;

import be.refleqt.selenium.support.ScenarioManager;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import net.jodah.failsafe.Failsafe;
import org.apache.commons.lang.StringUtils;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.ui.Select;

public class RefleqtWebElement extends RefleqtElementConfiguration implements WebElement {

    /****************
     Global Variables
     *****************/
    private final WebElement element;
    private final WebDriver driver;
    private final ElementLocator locator;

    /****************
     Constructor
     *****************/
    public RefleqtWebElement(final WebElement element, final WebDriver driver, ElementLocator locator) {
        this.element = element;
        this.driver = driver;
        this.locator = locator;
    }

    /****************
     Interactions
     *****************/
    @Override
    public void click() {
        Failsafe.with(retryPolicy).run(() ->
        {
            waitForAngularLoad();
            waitForElementToBeVisible();
            element.click();
            waitForAngularLoad();
        });
    }

    public void clickAndHold() {
        waitForElementToBeVisible();
        new Actions(driver)
                .moveToElement(getWebElement(), 0, 0)
                .clickAndHold(getWebElement())
                .perform();
    }

    public void rightClick() {
        waitForElementToBeVisible();
        new Actions(driver)
                .moveToElement(getWebElement(), 0, 0)
                .contextClick(getWebElement())
                .perform();
    }

    public void doubleClick() {
        waitForElementToBeVisible();
        new Actions(driver)
                .moveToElement(getWebElement(), 0, 0)
                .doubleClick(getWebElement())
                .perform();
    }

    public void clickWithJavaScript() {
        waitForAngularLoad();
        waitForElementToBeLocatable();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", getWebElement());
    }

    @Override
    public void submit() {
        element.submit();
    }

    @Override
    public void sendKeys(CharSequence... charSequences) {
        Failsafe.with(retryPolicy).run(() ->
        {
            waitForAngularLoad();
            waitForElementToBeVisible();
            element.click();
            new Actions(driver)
                    .sendKeys(charSequences)
                    .perform();
            waitForAngularLoad();
            ScenarioManager.saveScreenshot();
        });
    }

    public void sendKeysSplitUp(String charSequences, int splitLength) {
        StringBuilder regex = new StringBuilder();
        regex.append("(?<=\\G");
        for (int i = 0; i < splitLength; i++) {
            regex.append(".");
        }
        regex.append(")");

        for (String splitWord : charSequences.split(regex.toString())) {
            sendKeys(splitWord);
        }
    }

    public void sendKeysToHiddenElement(CharSequence... charSequences) {
        Failsafe.with(retryPolicy).run(() ->
        {
            waitForAngularLoad();
            waitForElementToBeLocatable();
            element.sendKeys(charSequences);
        });
    }

    @Override
    public void clear() {
        Failsafe.with(retryPolicy).run(() ->
        {
            waitForElementToBeVisible();
            element.clear();
        });
    }

    public void mouseOver() {
        waitForElementToBeVisible();
        try {
            new Actions(driver)
                    .moveToElement(element)
                    .build().perform();
        } catch (ClassCastException e) {
            new Actions(driver)
                    .moveToElement(getWebElement())
                    .build().perform();
        }
    }

    public void mouseOver(int xOffset, int yOffset) {
        waitForElementToBeVisible();
        try {
            new Actions(driver)
                    .moveToElement(element, xOffset, yOffset)
                    .build().perform();
        } catch (ClassCastException e) {
            new Actions(driver)
                    .moveToElement(getWebElement(), xOffset, yOffset)
                    .build().perform();
        }
    }

    public void selectByVisibleTextFromDropdown(String valueToSelect) {
        Failsafe.with(retryPolicy).run(() ->
        {
            Select select = new Select(element);
            select.selectByVisibleText(valueToSelect);
        });
    }

    public void selectByValueFromDropdown(String valueToSelect) {
        Failsafe.with(retryPolicy).run(() ->
        {
            Select select = new Select(element);
            select.selectByValue(valueToSelect);
        });
    }

    public void selectByIndexFromDropdown(int index) {
        Failsafe.with(retryPolicy).run(() ->
        {
            Select select = new Select(element);
            select.selectByIndex(index);
        });
    }

    public void setNumberField(int amountToSet) {
        WebElement element = getWebElement();
        while (!element.getAttribute("value").equalsIgnoreCase(String.valueOf(amountToSet))) {
            int value = Integer.parseInt(element.getAttribute("value"));

            Actions actions = new Actions(driver);
            actions.moveToElement(element)
                    .click();

            if (value < amountToSet) {
                actions.sendKeys(Keys.ARROW_UP);
            } else {
                actions.sendKeys(Keys.ARROW_DOWN);
            }

            actions
                    .build()
                    .perform();
        }
    }

    /****************
     Getters
     *****************/
    @Override
    public String getTagName() {
        return element.getTagName();
    }

    @Override
    public String getAttribute(String s) {
        return getWebElement().getAttribute(s);
    }

    @Override
    public boolean isSelected() {
        return element.isSelected();
    }

    @Override
    public boolean isEnabled() {
        return element.isEnabled();
    }

    @Override
    public String getText() {
        return element.getText();
    }

    @Override
    public boolean isDisplayed() {
        return element.isDisplayed();
    }

    @Override
    public Point getLocation() {
        return element.getLocation();
    }

    @Override
    public Dimension getSize() {
        return element.getSize();
    }

    @Override
    public Rectangle getRect() {
        return element.getRect();
    }

    @Override
    public String getCssValue(String s) {
        return element.getCssValue(s);
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
        return element.getScreenshotAs(outputType);
    }

    public WebElement getWebElement() {
        return locator.findElement();
    }

    public Boolean isPresent() {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean isPresentWait() {
        try {
            waitForShortElementToBeVisible();
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean isLocatable() {
        try {
            return element.getLocation() != null;
        } catch (Exception e) {
            return false;
        }
    }

    /****************
     Element locators
     *****************/
    @Override
    public List<WebElement> findElements(By by) {
        return element.findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        return element.findElement(by);
    }

    public void setAttribute(String attributeKey, String attributeValue) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute(arguments[1], arguments[2]);",
                getWebElement(),
                attributeKey,
                attributeValue);
    }

    /****************
     Scroll methods
     *****************/
    public RefleqtWebElement scrollIntoView() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", getWebElement());
        return this;
    }

    public RefleqtWebElement scrollIntoCenter() {
        String scrollElementIntoMiddle = "var viewPortHeight = Math.max(document.documentElement.clientHeight, " +
                "window.innerHeight || 0);var elementTop = arguments[0].getBoundingClientRect().top;"
                + "window.scrollBy(0, elementTop-(viewPortHeight/2));";

        ((JavascriptExecutor) driver).executeScript(scrollElementIntoMiddle, getWebElement());
        return this;
    }

    public void scrollIfNotInViewport() {
        if (!isInViewport()) {
            scrollIntoCenter();
        }
    }

    public boolean isInViewport() {
        return (Boolean) ((JavascriptExecutor) driver).executeScript(
                "var rect = arguments[0].getBoundingClientRect();                                                " +
                        "    return (                                                                               " +
                        "        rect.top >= 0 &&                                                                   " +
                        "        rect.left >= 0 &&                                                                  " +
                        "        rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) &&    " +
                        "        rect.right <= (window.innerWidth || document.documentElement.clientWidth)          " +
                        "    );                                                                                     "
                , getWebElement());
    }

    /****************
     waitFor
     *****************/
    public void waitForElementToBeEnabled() {
        await().atMost(timeOut, TimeUnit.SECONDS)
                .ignoreExceptions()
                .until(element::isEnabled);
    }

    public void waitForElementToBeLocatable() {
        await().atMost(timeOut, TimeUnit.SECONDS)
                .ignoreExceptions()
                .until(this::isLocatable);
    }

    public void waitForElementToBeVisible() {
        waitForVisibility(timeOut);
    }

    private void waitForVisibility(long timeOut) {
        await().atMost(timeOut, TimeUnit.SECONDS)
                .ignoreExceptions()
                .until(this::checkFrames);
        scrollIfNotInViewport();
    }

    public void waitForShortElementToBeVisible() {
        waitForVisibility(shortTimeOut);
    }

    public void waitForElementToBeInvisible() {
        await().atMost(timeOut, TimeUnit.SECONDS)
                .ignoreExceptions()
                .until(() -> !isPresent());
    }

    public void waitForLongElementToBeVisible() {
        waitForVisibility(longTimeOut);
    }

    public void waitForElementToContain(String termToContain) {
        await().atMost(longTimeOut, TimeUnit.SECONDS)
                .ignoreExceptions()
                .until(() -> StringUtils.containsIgnoreCase(element.getText(), termToContain));
    }

    /****************
     Helper Methods
     *****************/
    private Boolean checkFrames() {
        if (!isPresent()) {
            List<WebElement> frames = getFrames();
            if (frames.size() > 0) {
                //While is needed since other frames become stale so you can't foreach them.
                for (int i = 0; i < frames.size(); frames = this.getFrames(), i++) {
                    if (isPresent()) {
                        return true;
                    }

                    try {
                        driver.switchTo().frame(frames.get(i));
                    } catch (StaleElementReferenceException ignored) {
                    }

                    if (isPresent()) {
                        return true;
                    }

                    List<WebElement> innerFrames = getFrames();

                    if (innerFrames.size() > 0) {
                        try {
                            driver.switchTo().frame(innerFrames.get(0));
                        } catch (StaleElementReferenceException ignored) {
                        }

                        if (isPresent()) {
                            return true;
                        }
                    }

                    driver.switchTo().defaultContent();
                }
            }
        }
        //Making sure to return to the defaultContent if the element is not found in the frames.
        driver.switchTo().defaultContent();
        return isPresent();
    }

    private List<WebElement> getFrames() {
        List<WebElement> frames = driver.findElements(By.cssSelector("iframe"));
        Set<WebElement> filteredFrames = new LinkedHashSet<>();

        try {
            filteredFrames.addAll(frames
                    .stream()
                    .filter(f -> f.getAttribute("name") != null)
                    .filter(f -> !f.getAttribute("name").isEmpty())
                    .collect(Collectors.toSet()));

            filteredFrames.addAll(frames
                    .stream()
                    .filter(f -> f.getAttribute("id") != null)
                    .filter(f -> !f.getAttribute("id").isEmpty())
                    .collect(Collectors.toSet()));

            frames.clear();

            frames.addAll(filteredFrames);
        } catch (Exception e) {
            frames = new ArrayList<>();
        }

        return frames;
    }

    /****************
     Validations
     *****************/
    public void validatePresence() {
        validatePresence(true);
    }

    public void validatePresence(Boolean isPresent) {
        assertThat(isPresentWait()).isEqualTo(isPresent);
    }

    public void validateTextToContain(String toContain) {
        waitForElementToBeVisible();
        assertThat(element.getText())
                .containsIgnoringCase(toContain);
    }

    public void validateTextToMatch(String toMatch) {
        waitForElementToBeVisible();
        assertThat(element.getText())
                .isEqualTo(toMatch);
    }

    public void validateInnerTextToContain(String toContain) {
        waitForElementToBeVisible();
        assertThat(getWebElement().getAttribute("innerText"))
                .containsIgnoringCase(toContain);
    }

    public void validateInnerHTMLToContain(String toContain) {
        waitForElementToBeVisible();
        assertThat(getWebElement().getAttribute("innerHTML"))
                .containsIgnoringCase(toContain);
    }

    public void validateInViewPort() {
        waitForElementToBeLocatable();
        assertThat(isInViewport()).isTrue();
    }

    public void validateInViewPort(boolean isInPort) {
        waitForElementToBeLocatable();
        assertThat(isInViewport()).isEqualTo(isInPort);
    }
}
