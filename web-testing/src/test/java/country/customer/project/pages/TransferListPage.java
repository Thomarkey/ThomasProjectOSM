package country.customer.project.pages;

import country.customer.project.selenium.driver.element.RefleqtListOfWebElements;
import country.customer.project.selenium.driver.element.RefleqtWebElement;
import country.customer.project.support.CsvWriter;
import country.customer.project.support.World;
import country.customer.project.support.WorldHelper;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.support.FindBy;

import java.io.IOException;
import java.util.ArrayList;

public class TransferListPage extends AbstractPage {
    //omdat WorldHelper.getWorld() zoveel gebruikt wordt:
    World world = WorldHelper.getWorld();


    //tabel selecteren en dan navigeren naar de elementen dager uit wilt halen
    // voor elke table row
    // me ne foreach

    @FindBy(css = "#transfer-list > div > table > tbody:nth-child(2)")
    RefleqtWebElement transferListTable;

    @FindBy(css = "#transfer-list > div > table > tbody:nth-child(2) > tr:nth-child(1) > td.vertical-center")
    RefleqtWebElement firstPlayer;

    @FindBy(css = "#transfer-list > div > table > tbody:nth-child(2) > tr:nth-child(2) > td.vertical-center")
    RefleqtWebElement secondPlayer;

    @FindBy(css = "#modal-dialog-buyforeignplayer > div > div > div.row.theme-panna-1.player-profile-player > div > div.row.row-h-xs-6 > div > div > h2")
    RefleqtWebElement firstPlayerName;

    @FindBy(css = "#modal-dialog-buyforeignplayer > div > div > div.row.theme-panna-1.player-profile-player > div > div.row.row-h-xs-18 > div:nth-child(3) > div:nth-child(3) > div > div > h3")
    RefleqtWebElement firstPlayerValue;

    @FindBy(css = "#modal-dialog-buyforeignplayer > div > div > div.row.theme-panna-1.player-profile-details.player-profile-button > div > div > button > span > bdi > span > span.boss-coin-price")
    RefleqtWebElement firstPlayerCost;

    @FindBy(css = "#modal-dialog-buyforeignplayer > div > div > div.row.theme-panna-1.player-profile-player > div > div.row.row-h-xs-6 > div > div > h2")
    RefleqtWebElement secondPlayerName;

    @FindBy(css = "#modal-dialog-buyforeignplayer > div > div > div.row.theme-panna-1.player-profile-player > div > div.row.row-h-xs-18 > div:nth-child(3) > div:nth-child(3) > div > div > h3")
    RefleqtWebElement secondPlayerValue;

    @FindBy(css = "#modal-dialog-buyforeignplayer > div > div > div.row.theme-panna-1.player-profile-details.player-profile-button > div > div > button > span > bdi > span > span.boss-coin-price")
    RefleqtWebElement secondPlayerCost;

    @FindBy(css = "div > table > tbody:nth-child(2) > tr:nth-child(1) > td.vertical-center")
    RefleqtWebElement eerstePlayerRow;


    @FindBy(css = "#transfer-list > div > table")
    RefleqtWebElement transferTable;

    @FindBy(css = "tr")
    RefleqtListOfWebElements transferTableRows;

    @FindBy(css = "#modal-dialog-buyforeignplayer > div > div > div.row.theme-panna-1.player-profile-details.player-profile-button > div > div > button > span > bdi > span > span.boss-coin-price")
    RefleqtWebElement bossCoinPrice;
    ///////////////////////////////////////////////

    @FindBy(css = ".close")
    RefleqtWebElement closeDialog;

    @FindBy(css = ".player-table-row")
    RefleqtListOfWebElements playerTableRows;

    @FindBy(css = ".player-table-row .semi-bold[data-bind*=name]")
    RefleqtListOfWebElements playerName;

    @FindBy(css = ".player-table-row [data-bind*=age]")
    RefleqtListOfWebElements playerAge;

    @FindBy(css = ".player-table-row [data-bind*=price]")
    RefleqtListOfWebElements playerPrice;

    @FindBy(css = ".player-profile-player [data-bind*=value]")
    RefleqtWebElement playerValue;

    public void getTable() throws InterruptedException, IOException {
        Thread.sleep(2000);
        int rowAantalPlayers = playerTableRows.size();

        world.stringList = new ArrayList<>();

        for (int i = 0; i < rowAantalPlayers; i++) {
            playerTableRows.get(i).scrollIntoCenter();
            String name = playerName.get(i).getAttribute("innerText");
            String age = playerAge.get(i).getAttribute("innerText");
            String price = playerPrice.get(i).getAttribute("innerText").replace(",",".");

            playerTableRows.get(i).clickWithJavaScript();
            String value = playerValue.getAttribute("innerText").replace(",",".");
            closeDialog.clickCoordinates();

            world.stringList.add(name  + ";" + age + ";" + value  + ";" + price);
        }
        //+ "\n"

    }


}
