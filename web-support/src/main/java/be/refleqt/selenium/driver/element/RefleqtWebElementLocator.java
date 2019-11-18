package be.refleqt.selenium.driver.element;

import java.lang.reflect.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.pagefactory.*;

public class RefleqtWebElementLocator implements ElementLocatorFactory {

    private final SearchContext context;

    public RefleqtWebElementLocator(WebDriver driver){
        this.context = driver;
    }

    public ElementLocator createLocator(Field field) {
        return new DefaultElementLocator(context, field);
    }
}