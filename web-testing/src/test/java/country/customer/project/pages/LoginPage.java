package country.customer.project.pages;

import country.customer.project.selenium.driver.element.RefleqtWebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends AbstractPage {

    @FindBy(css = "#page-privacynotice > div > div > div > div:nth-child(2) > div.privacynotice-container.row.overflow-visible > div:nth-child(2) > div > div > div > label > p")
    RefleqtWebElement acceptTerms;

    @FindBy(css = "#page-privacynotice > div > div > div > div.col-xs-12.horizontal-center.agree-button-container > button")
    RefleqtWebElement doorgaanBtn;

    @FindBy(id = "login-link")
    RefleqtWebElement loginAccount;

    @FindBy(id = "manager-name")
    RefleqtWebElement managerNameField;

    @FindBy(id = "password")
    RefleqtWebElement passwordField;

    @FindBy(id = "login")
    RefleqtWebElement loginBtn;

    public LoginPage acceptTerms() {
        acceptTerms.click();
        return this;
    }

    public LoginPage doorgaan() {
        doorgaanBtn.click();
        return this;
    }

    public LoginPage inloggenAccount() {
        loginAccount.click();
        return this;
    }

    public LoginPage fillInCredentials() {
        managerNameField.sendKeys("abdesalami");
        passwordField.sendKeys("markey");
        return this;
    }

    public void logIn() throws InterruptedException {
        loginBtn.click();
        Thread.sleep(1000);
    }

}
