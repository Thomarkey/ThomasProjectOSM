package be.refleqt.projectname.steps;

import be.refleqt.projectname.pages.ECommercePage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ECommerceSteps {

    @When("^I add the first bestseller item to my cart")
    public void addFirstItem() {
        new ECommercePage()
                .clickBestSeller()
                .mouseOverFirstProduct()
                .clickAddToCart();
    }

    @Then("I successfully added something to my cart")
    public void iSuccessfullyAddedSomethingToMyCart() {
        new ECommercePage()
                .validateCartPopup();
    }
}
