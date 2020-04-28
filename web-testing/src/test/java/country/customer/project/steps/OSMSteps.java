package country.customer.project.steps;

import country.customer.project.pages.*;
import country.customer.project.selenium.driver.element.RefleqtWebElement;
import country.customer.project.support.CsvWriter;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.support.FindBy;

import java.io.IOException;

public class OSMSteps {

    @FindBy(css = ".toast-container [data-bind=animate: isVisible]")
    RefleqtWebElement bosscoinBonus;
    //nullpointer
    //> div > div.toast-container> div > div > div > div.toastContentContainer > div > div > div > h3

    @And("I login with my credentials")
    public void iLoginWithMyCredentials() throws InterruptedException {
        LoginPage loginPage = new LoginPage();
        loginPage
                .acceptTerms()
                .doorgaan()
                .inloggenAccount()
                .fillInCredentials()
                .logIn();
    }

    @And("I choose my team")
    public void iChooseMyTeam() throws InterruptedException {
        Thread.sleep(3000);
        new CareerPage().chooseTeam();
    }

    @And("I navigate to the transferlist")
    public void iNavigateToTheTransferlist() {
        new OverviewPage().goToTransferlist();
    }

    @And("I navigate to my selectie")
    public void iNavigateToMySelectie() {
        new OverviewPage().goToSelectie();
    }

    @And("I browse the transferlist")
    public void iBrowseTheFirstPlayer() throws InterruptedException, IOException {
        new TransferListPage().getTable();
//        new TransferListPage().loopTable();
//        new TransferListPage().getFirstPlayer();
//        Thread.sleep(3000);
//        new TransferListPage().getSecondPlayer();
//        Thread.sleep(3000);

    }

    @And("I create a CSV from all the players in the transferlist")
    public void iCreateACSVFromAllThePlayersInTheTransferlist() throws InterruptedException, IOException {
        new CsvWriter().writeCSV();
    }

    @When("I browse my team")
    public void iBrowseMyTeam() throws InterruptedException {
        new SelectiePage().getSelectieTable();
    }

    @Then("I sell the right people")
    public void iSellTheRightPeople() throws InterruptedException {
        new SelectiePage().addSelectedPlayersToTransferlist();
    }

//    @And("I claim possible bosscoin")
//    public void iClaimPossibleBosscoin() throws InterruptedException {
//        Thread.sleep(6000);
//       if(bosscoinBonus.isPresent()){
//           bosscoinBonus.scrollIntoCenter();
//       }else
//    }

}
