package country.customer.project.pages;

import country.customer.project.selenium.driver.element.RefleqtWebElement;
import country.customer.project.support.WorldHelper;
import org.openqa.selenium.support.FindBy;

public class HomePage extends AbstractPage {

    @FindBy(name = "q")
    private RefleqtWebElement searchTxtFld;

    @FindBy(name = "btnK")
    private RefleqtWebElement searchBtn;

    @FindBy(css = "#rso > div > div > div:nth-child(1) > div > div > div.r > a:nth-child(1) > h3")
    private RefleqtWebElement firstSearchResultLbl;

    public HomePage enterSearch(String value) {
        searchTxtFld.clear();
        searchTxtFld.sendKeys(value);
        return this;
    }

    public void clickSearch() {
        searchBtn.click();
    }

    public void validateFirstSearchItem(String value) {
        firstSearchResultLbl.validateTextToMatch(value);
    }

    public HomePage getFirstSearchItem() {
        firstSearchResultLbl.waitForElementToBeVisible();
        WorldHelper.getWorld().result = firstSearchResultLbl.getText();
        return this;
    }
}
