package country.customer.project.pages;

import country.customer.project.selenium.driver.element.RefleqtWebElement;
import org.openqa.selenium.support.FindBy;

public class HighLightPage extends AbstractPage {

    @FindBy(css = "#content > div > h4:nth-child(5)")
    RefleqtWebElement tile;

    public void clickTile() throws InterruptedException {
        tile.highlightElement();
    }

}
