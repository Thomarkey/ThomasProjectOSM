package country.customer.project.selenium.driver.element;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import static java.lang.reflect.Proxy.newProxyInstance;
import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.*;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementHandler;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementListHandler;

public class RefleqtWebFieldDecorator implements FieldDecorator {
    protected ElementLocatorFactory factory;
    protected WebDriver driver;

    public RefleqtWebFieldDecorator(WebDriver driver) {
        this.factory = new RefleqtWebElementLocator(driver);
        this.driver = driver;
    }

    public Object decorate(ClassLoader loader, Field field) {
        ElementLocator locator = factory.createLocator(field);
        if (locator == null) {
            return null;
        }
        if (WebElement.class.isAssignableFrom(field.getType())) {
            return proxyForLocator(loader, locator);
        } else if (RefleqtListOfWebElements.class.isAssignableFrom(field.getType())) {
            return proxyForListLocator(loader, locator);
        } else {
            return null;
        }
    }

    protected Object proxyForLocator(ClassLoader loader, ElementLocator locator) {
        InvocationHandler handler = new LocatingElementHandler(locator);
        WebElement proxy = (WebElement) newProxyInstance(loader, new Class[]{WebElement.class}, handler);

        return new RefleqtWebElement(proxy, driver, locator);
    }

    protected RefleqtListOfWebElements proxyForListLocator(ClassLoader loader, ElementLocator locator) {
        InvocationHandler handler = new LocatingElementListHandler(locator);
        List<WebElement> proxy = (List<WebElement>) newProxyInstance(loader, new Class[]{List.class}, handler);

        return new RefleqtListOfWebElements(proxy, driver, locator);
    }
}
