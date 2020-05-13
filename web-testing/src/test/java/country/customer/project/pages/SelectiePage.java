package country.customer.project.pages;

import country.customer.project.selenium.driver.element.RefleqtListOfWebElements;
import country.customer.project.selenium.driver.element.RefleqtWebElement;
import country.customer.project.support.World;
import country.customer.project.support.WorldHelper;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.Collections;

public class SelectiePage extends AbstractPage {

    World world = WorldHelper.getWorld();

    @FindBy(css = "#squad > div > #squad-table .player-table-row")
    RefleqtListOfWebElements playerTableRows;

    @FindBy(css = ".player-table-row .semi-bold[data-bind*=name]")
    RefleqtListOfWebElements playerName;

    @FindBy(css = ".player-table-row [data-bind*=currency]")
    RefleqtListOfWebElements playerPrice;

    @FindBy(css = "#modal-dialog-sellplayer > div > div > div.row.theme-panna-1.player-profile-details.player-profile-button > div > div > a")
    RefleqtWebElement setPlayerTransferList;

    @FindBy(css = "#modal-dialog-sellplayer > div > div > div.row.theme-panna-1.player-profile-details.player-profile-setprice > div.col-xs-12.col-h-xs-8.center.text-center > div > div > div.slider-handle.min-slider-handle.round")
    RefleqtWebElement priceSliderSelector;

    @FindBy(css = "#modal-dialog-sellplayer > div > div > div.row.theme-panna-1.player-profile-details.player-profile-setprice > div.col-xs-12.col-h-xs-8.center.text-center > div > div")
    RefleqtWebElement priceSlider;

    @FindBy(css = "#modal-dialog-sellplayer > div > div > div.row.theme-panna-1.player-profile-details.player-profile-setprice > div.col-xs-12.col-h-xs-8.center.text-center > div > div > div.slider-handle.max-slider-handle.round.hide")
    RefleqtWebElement maxPriceSlider;

    @FindBy(css = "#modal-dialog-sellplayer > div > div > div.row.theme-panna-1.player-profile-details.player-profile-setprice > div.col-xs-12.col-h-xs-8.center.text-center > div > input")
    RefleqtWebElement hiddenPriceSliderLocator;

    @FindBy(css = "#modal-dialog-sellplayer > div > div > div.row.theme-panna-1.player-profile-details.player-profile-setprice > div.col-xs-12.col-h-xs-8.center.text-center > div > div > div.slider-track")
    RefleqtWebElement slider;

    @FindBy(css = "#modal-dialog-sellplayer > div > div > div.row.theme-panna-1.player-profile-details.player-profile-button > div > div > a")
    RefleqtWebElement bevestigSale;

    @FindBy(css = "#modal-dialog-alert > div.row.row-h-xs-24.overflow-visible.modal-content-container")
    RefleqtWebElement popupTransfer;

    @FindBy(css = "#modal-dialog-alert > div.row.row-h-xs-24.overflow-visible.modal-content-container > div > div > div > div.modal-header > h3")
    RefleqtWebElement popupTransferOeps;

    public void getSelectieTable() throws InterruptedException {
        Thread.sleep(1000);

        int rowAantalPlayers = playerTableRows.size();
        world.selectieList = new ArrayList<>();
        world.selectieListPrices = new ArrayList<>();
        world.minValuesList = new ArrayList<>();

        for (int i = 0; i < rowAantalPlayers; i++) {
            playerTableRows.get(i).scrollIntoView();
            String price = (playerPrice.get(i).getText().replace("M", "00000").replace("K", ""));
            world.selectieList.add(price);

        }
        world.selectieListPrices = getIntegerArray(world.selectieList);
    }

    public void addSelectedPlayersToTransferlist() throws InterruptedException {

        for (int i = 1; i < 5; i++) {
            int minIndex = world.selectieListPrices.indexOf(Collections.min(world.selectieListPrices));
            playerTableRows.get(minIndex).scrollIntoCenter().clickWithJavaScript();

            if (setPlayerTransferList.isLocatable()) {
                setPlayerTransferList.clickWithJavaScript();

                Dimension sliderSize = priceSlider.getSize();
                int sliderWidth = sliderSize.getWidth();

                Actions builder = new Actions(driver);
                builder.moveToElement(priceSlider)
                        .click()
                        .dragAndDropBy(priceSlider, sliderWidth, 0)
                        .build()
                        .perform();
                bevestigSale.clickWithJavaScript();
                world.selectieListPrices.set(minIndex, 200000000);
                if (popupTransferOeps.isLocatable()) {
                    i = i - 1;
                } else {
                    System.out.println("player op transferlist");
                }
            } else {
                i = i - 1;
                world.selectieListPrices.set(minIndex, 200000000);
            }
        }
        System.out.println(world.minValuesList);
        Thread.sleep(5000);
    }


    public ArrayList<Integer> getIntegerArray(ArrayList<String> stringArray) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        for (String stringValue : stringArray) {
            result.add(Integer.parseInt(stringValue.replaceAll(",", "")));
        }
        return result;
    }

}
