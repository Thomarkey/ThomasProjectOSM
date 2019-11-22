package be.refleqt.projectname.steps;

import be.refleqt.appium.driver.DriverProvider;
import be.refleqt.appium.support.ScenarioManager;
import be.refleqt.projectname.pages.page.HomePage;
import io.cucumber.core.api.Scenario;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;

public class CommonSteps {

    @Before
    public void setup(Scenario scenario) {
        ScenarioManager.setScenario(scenario);
    }

    @After
    public void tearDown() {
        ScenarioManager.saveScreenshot();
        DriverProvider.getDriver().quit();
    }

    @When("I create a new task called {}")
    public void pressSearch(String term) {
        new HomePage()
                .clickCreateNewTask()
                .enterTask(term)
                .clickConfirm();
    }

    @When("I check all the checkboxes")
    public void iCheckAllTheCheckboxes() {
        new HomePage().checkAllCheckboxes(true);
    }

    @And("I edit the last task to {}")
    public void iEditTheLastTaskTo(String term) {
        new HomePage()
                .selectLastTask()
                .clickEdit()
                .enterTask(term)
                .clickConfirm();
    }

    @And("I delete the last task")
    public void iDeleteTheLastTask() {
        new HomePage()
                .selectLastTask()
                .clickDelete();
    }

    @Then("I wait for {int} seconds")
    public void iWaitForSeconds(int arg0) throws InterruptedException {
        Thread.sleep(arg0 * 1000);
    }

    @Given("I launch the app")
    public void iLaunchTheApp() {
        DriverProvider.setUpDriver();
    }
}
