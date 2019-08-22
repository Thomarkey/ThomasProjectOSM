package be.refleqt.projectname.steps;

import be.refleqt.library.selenium.DriverProvider;
import be.refleqt.library.selenium.ScenarioManager;
import be.refleqt.projectname.pages.HomePage;
import be.refleqt.projectname.support.World;
import io.cucumber.core.api.Scenario;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CommonSteps {

    private World world;

    public CommonSteps(World world) {
        this.world = world;
    }

    @Before
    public void beforeScenario(Scenario scenario) {
        ScenarioManager.setScenario(scenario);
        DriverProvider.setUpDriver();
    }

    @After
    public void afterScenario() {
        ScenarioManager.saveScreenshot();
        DriverProvider.tearDownDriver();
    }

    @Given("I go to {}")
    public void iGoTo(String website) {
        DriverProvider.goToURL(website);
    }

    @When("I search for {}")
    public void iLookFor(String value) throws Throwable {
        world.search = value;
        new HomePage().enterSearch(value)
                .clickSearch();
    }

    @Then("The first result is {}")
    public void theFirstResult(String value) throws Throwable {
        new HomePage().getFirstSearchItem(world).validateFirstSearchItem(value);
    }
}
