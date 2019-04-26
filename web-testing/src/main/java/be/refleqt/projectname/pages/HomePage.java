package be.refleqt.projectname.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class HomePage extends AbstractPage {

    @FindBy(name = "q")
    private WebElement searchTxtFld;

    @FindBy(name = "btnK")
    private WebElement searchBtn;

    @FindBy(css = "#rso > div > div > div:nth-child(1) > div > div > div.r > a:nth-child(1) > h3")
    private WebElement firstSearchResultLbl;

    public HomePage enterSearch(String value) {
        sendKeysToElement(searchTxtFld, value);
        return this;
    }

    public void clickSearch() {
        clickOnElement(searchBtn);
    }

    public void validateFirstSearchItem(String value) {
        waitForElementToBeVisible(firstSearchResultLbl);
        assertThat(firstSearchResultLbl.getText()).isEqualToIgnoringCase(value);
    }
}
