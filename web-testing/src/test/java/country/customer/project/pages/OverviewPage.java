package country.customer.project.pages;

import country.customer.project.selenium.driver.element.RefleqtWebElement;
import org.openqa.selenium.support.FindBy;

public class OverviewPage extends AbstractPage {
    @FindBy(css = "#desktop-menu-navbar > li:nth-child(2) > a > div.menu-title")
    RefleqtWebElement teamHeader;

    @FindBy(css = "#desktop-menu-navbar > li.dropdown.border.open > ul > li:nth-child(5) > a > span")
    RefleqtWebElement transferlist;

    @FindBy(css = "#desktop-menu-navbar > li.dropdown.border.open > ul > li:nth-child(1) > a > span")
    RefleqtWebElement selectie;

    public void goToTransferlist() {
        teamHeader.click();
        transferlist.click();
    }

    public void goToSelectie(){
        teamHeader.click();
        selectie.click();
    }
}
