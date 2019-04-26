package be.refleqt.projectname.pages;

import be.refleqt.projectname.support.PropertiesReader;
import org.openqa.selenium.WebElement;

/**
 * In this class you can override the already defined functions locally
 */
public class AbstractPage extends be.refleqt.library.selenium.AbstractPage {

    @Override
    protected void waitforElementToBeClickable(WebElement element) {
        super.waitforElementToBeClickable(element);
    }

    public AbstractPage() {
        super();
        setTimeOut(Long.parseLong(PropertiesReader.getKey("timeout.explicitWait")));
        setShortTimeOut(Long.parseLong(PropertiesReader.getKey("timeout.implicitWait.short")));
        setLongTimeOut(Long.parseLong(PropertiesReader.getKey("timeout.longWait")));
    }
}
