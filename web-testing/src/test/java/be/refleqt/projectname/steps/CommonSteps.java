package be.refleqt.projectname.steps;

import be.refleqt.library.selenium.DriverProvider;
import be.refleqt.library.selenium.ScenarioManager;
import be.refleqt.projectname.pages.HomePage;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CommonSteps {
    @Before
    public void beforeScenario(Scenario scenario) {
        DriverProvider.setUpDriver();
        ScenarioManager.setScenario(scenario);
    }

    @After
    public void afterScenario() {
        ScenarioManager.saveScreenshot();
        DriverProvider.tearDownDriver();
    }

    @Given("I go to (.*)")
    public void iGoTo(String website) {
        DriverProvider.goToURL(website);
    }

    @When("^I search for (.*)")
    public void iLookFor(String value) throws Throwable {
        new HomePage().enterSearch(value)
                .clickSearch();
    }

    @Then("^The first result is (.*)")
    public void theFirstResult(String value) throws Throwable {
        new HomePage().validateFirstSearchItem(value);
    }
}
