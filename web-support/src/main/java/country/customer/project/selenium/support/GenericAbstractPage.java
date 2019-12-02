package country.customer.project.selenium.support;

import country.customer.project.selenium.driver.DriverProvider;
import country.customer.project.selenium.driver.element.RefleqtElementConfiguration;
import country.customer.project.selenium.driver.element.RefleqtWebFieldDecorator;
import static org.assertj.core.api.Assertions.assertThat;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

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
