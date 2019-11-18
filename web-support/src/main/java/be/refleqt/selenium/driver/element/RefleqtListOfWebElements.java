package be.refleqt.selenium.driver.element;

import java.util.*;
import java.util.concurrent.*;
import net.jodah.failsafe.*;
import org.apache.commons.lang.*;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.*;
import static org.hamcrest.Matchers.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.*;
import org.openqa.selenium.support.pagefactory.*;

public class RefleqtListOfWebElements extends RefleqtElementConfiguration {

    /****************
     Global Variables
     *****************/
    private final List<WebElement> webElements;
    private final WebDriver driver;
    private final ElementLocator locator;

    /****************
     Constructor + Setter
     *****************/
    private RefleqtListOfWebElements() {
        webElements = new ArrayList<>();
        driver = null;
        locator = null;
    }

    public RefleqtListOfWebElements(List<WebElement> webElements, WebDriver driver, ElementLocator locator) {
        this.webElements = webElements;
        this.driver = driver;
        this.locator = locator;
    }

    /****************
     Getters
     *****************/
    public RefleqtWebElement get(int index) {
        waitForElementsToBeVisible();
        return new RefleqtWebElement(
                webElements.get(index),
                driver,
                locator
        );
    }

    public int size() {
        return webElements.size();
    }

    public List<WebElement> getWebElements() {
        return webElements;
    }

    public RefleqtWebElement getFirstDisplayed() {
        waitForElementsToBeVisible();
        return new RefleqtWebElement(
                webElements
                        .stream()
                        .filter(e -> new RefleqtWebElement(e, driver, locator).isPresent())
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException("None of the list are displayed!")),
                driver,
                locator
        );
    }

    public RefleqtWebElement getWithAttributeContaining(String attribute, String value) {
        waitForElementsToBeVisible();
        return new RefleqtWebElement(
                locator.findElements()
                        .stream()
                        .filter(e -> StringUtils.containsIgnoreCase(e.getAttribute(attribute), value))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException("None of the list contain this term!")),
                driver,
                locator
        );
    }

    public RefleqtWebElement getFirstContainingTerm(String termToContain) {
        waitForElementsToBeVisible();
        return new RefleqtWebElement(
                webElements
                        .stream()
                        .filter(e -> StringUtils.containsIgnoreCase(e.getText(), termToContain))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException("None of the list contain this term!")),
                driver,
                locator
        );
    }

    public Integer getIndexOfElementContainingTerm(String termToContain) {
        waitForElementsToBeVisible();
        for (int i = 0; i < webElements.size(); i++) {
            if (StringUtils.containsIgnoreCase(webElements.get(i).getText(), termToContain)) {
                return i;
            }
        }
        return null;
    }

    public Integer getIndexOfAttributeContainingTerm(String attribute, String termToContain) {
        waitForElementsToBeVisible();

        List<WebElement> localList = locator.findElements();

        for (int i = 0; i < localList.size(); i++) {
            if (StringUtils.containsIgnoreCase(localList.get(i).getAttribute(attribute), termToContain)) {
                return i;
            }
        }
        return null;
    }

    /****************
     Interactions
     *****************/
    public void click(int index) {
        Failsafe.with(retryPolicy).run(() -> {
            waitForElementsToBeVisible(index);
            webElements.get(index).click();
        });
    }

    public void click(String termToContain) {
        Failsafe.with(retryPolicy).run(() -> {
            waitForAngularLoad();
            waitForElementsToBeVisible();
            webElements
                    .stream()
                    .filter(element -> StringUtils.containsIgnoreCase(element.getText(), termToContain))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("The given webElements of elements does not contain the request filter!"))
                    .click();
            waitForAngularLoad();
        });
    }

    public void clickFirstDisplayed() {
        Failsafe.with(retryPolicy).run(() -> {
            waitForAngularLoad();
            getFirstDisplayed()
                    .click();
        });
    }

    /****************
     WaitFor
     *****************/
    public void waitForElementsToBeVisible() {
        waitForElementsToBeVisible(0);
    }

    public void waitForElementsToBeVisible(int index) {
        waitForVisibility(timeOut, index);
    }

    public void waitForShortElementToBeVisible() {
        waitForVisibility(shortTimeOut, 0);
    }

    private void waitForVisibility(long shortTimeOut, int index) {
        await().atMost(shortTimeOut, TimeUnit.SECONDS)
                .ignoreExceptions()
                .until(webElements::size, greaterThan(index));
    }

    /****************
     Validations
     *****************/
    public void validateAnyToContain(String toContain) {
        waitForElementsToBeVisible();
        assertThat(webElements
                .stream()
                .anyMatch(element -> StringUtils.containsIgnoreCase(element.getText(), toContain))
        ).isTrue();
    }

    public void validateAllToContain(String toContain) {
        waitForElementsToBeVisible();
        assertThat(webElements
                .stream()
                .allMatch(element -> StringUtils.containsIgnoreCase(element.getText(), toContain))
        ).isTrue();
    }

    public void validateNoneToContain(String toContain) {
        waitForElementsToBeVisible();
        assertThat(webElements
                .stream()
                .noneMatch(element -> StringUtils.containsIgnoreCase(element.getText(), toContain))
        ).isTrue();
    }
}
