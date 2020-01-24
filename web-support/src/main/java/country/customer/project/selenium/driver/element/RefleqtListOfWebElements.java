package country.customer.project.selenium.driver.element;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import net.jodah.failsafe.Failsafe;
import org.apache.commons.lang.StringUtils;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.greaterThan;
import org.openqa.selenium.*;

public class RefleqtListOfWebElements extends RefleqtElementConfiguration {

    /****************
     Global Variables
     *****************/
    private final List<WebElement> webElements;
    private final WebDriver driver;

    /****************
     Constructor + Setter
     *****************/
    private RefleqtListOfWebElements() {
        webElements = new ArrayList<>();
        driver = null;
    }

    public RefleqtListOfWebElements(List<WebElement> webElements, WebDriver driver) {
        this.webElements = webElements;
        this.driver = driver;
    }

    /****************
     Getters
     *****************/
    public List<RefleqtWebElement> getList() {
        List<RefleqtWebElement> elements = new ArrayList<>();

        webElements.forEach(element -> elements.add(new RefleqtWebElement(element, driver)));

        return elements;
    }

    public RefleqtWebElement get(int index) {
        waitForElementsToBeVisible(index);
        return new RefleqtWebElement(
                webElements.get(index),
                driver
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
                        .filter(e -> new RefleqtWebElement(e, driver).isPresent())
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException("None of the list are displayed!")),
                driver
        );
    }

    public RefleqtListOfWebElements getDisplayedList() {
        waitForElementsToBeVisible();
        return new RefleqtListOfWebElements(
                webElements
                        .stream()
                        .filter(e -> new RefleqtWebElement(e, driver).isPresent())
                        .collect(Collectors.toList()),
                driver
        );
    }

    public RefleqtWebElement getWithAttributeContaining(String attribute, String value) {
        waitForElementsToBeVisible();
        return new RefleqtWebElement(
                getList()
                        .stream()
                        .filter(e -> StringUtils.containsIgnoreCase(e.getAttribute(attribute), value))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException("None of the list contain this term!")),
                driver
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
                driver
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

        List<RefleqtWebElement> localList = getList();

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
                    .orElseThrow(() -> new NoSuchElementException(
                            "The given webElements of elements does not contain the request filter!"))
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
        assertThat(getList()
                .stream()
                .anyMatch(element -> StringUtils.containsIgnoreCase(element.getAttribute("innerText"), toContain))
        ).isTrue();
    }

    public void validateAllToContain(String toContain) {
        waitForElementsToBeVisible();
        assertThat(getList()
                .stream()
                .allMatch(element -> StringUtils.containsIgnoreCase(element.getAttribute("innerText"), toContain))
        ).isTrue();
    }

    public void validateNoneToContain(String toContain) {
        waitForElementsToBeVisible();
        assertThat(getList()
                .stream()
                .noneMatch(element -> StringUtils.containsIgnoreCase(element.getAttribute("innerText"), toContain))
        ).isTrue();
    }
}
