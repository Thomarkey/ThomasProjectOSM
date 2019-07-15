package be.refleqt.projectname.steps;

import be.refleqt.projectname.pages.ECommercePage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

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
