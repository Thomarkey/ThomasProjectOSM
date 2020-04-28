package country.customer.project.pages;

import country.customer.project.selenium.driver.element.RefleqtWebElement;
import org.openqa.selenium.support.FindBy;

public class CareerPage extends AbstractPage {

    @FindBy(css = "#mainAccount > div > div.row.row-h-xs-24.center.overflow-visible > div > div > div.pull-left.col-xs-12.col-h-xs-7.text-center > div > div > h2")
    RefleqtWebElement teamBtn;

    public void chooseTeam() {
        teamBtn.click();
    }

}
