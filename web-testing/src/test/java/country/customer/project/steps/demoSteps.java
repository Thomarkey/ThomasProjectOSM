package country.customer.project.steps;

import country.customer.project.selenium.driver.element.RefleqtWebElement;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class demoSteps {

    @FindBy(css = "#BooksAuthorsTable")
    RefleqtWebElement tableBooksAuthors;
//
//    @When("I loop the table")
//    public void iLoopTheTable() {
//
//        List<WebElement> rowVals = tableBooksAuthors.findElements(By.tagName("tr"));
//        rowVals.get(0).findElements(By.ByTagName())
//
//    }
}
