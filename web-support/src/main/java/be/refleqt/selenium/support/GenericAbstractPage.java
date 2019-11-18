package be.refleqt.selenium.support;

import be.refleqt.selenium.driver.*;
import be.refleqt.selenium.driver.element.*;
import static org.assertj.core.api.Assertions.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.*;

public abstract class GenericAbstractPage {

    protected final WebDriver driver;

    public long timeOut = 80;
    public long shortTimeOut = 40;
    public long longTimeOut = 160;


    public GenericAbstractPage() {
        this.driver = DriverProvider.getDriver();
        PageFactory.initElements(new RefleqtWebFieldDecorator(driver), this);
    }

    public void setTimeOut(long timeOut) {
        this.timeOut = timeOut;
        RefleqtElementConfiguration.setTimeOut(timeOut);
    }

    public void setShortTimeOut(long shortTimeOut) {
        this.shortTimeOut = shortTimeOut;
        RefleqtElementConfiguration.setShortTimeOut(shortTimeOut);
    }

    public void setLongTimeOut(long longTimeOut) {
        this.longTimeOut = longTimeOut;
        RefleqtElementConfiguration.setLongTimeOut(longTimeOut);
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
}
