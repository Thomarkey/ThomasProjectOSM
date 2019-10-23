package be.refleqt.projectname.pages;

import be.refleqt.library.selenium.driver.element.*;
import org.openqa.selenium.support.*;

public class ECommercePage extends AbstractPage {

    @FindBy(css = "[href=\"#blockbestsellers\"]")
    RefleqtWebElement bestSellerSectionBtn;

    @FindBy(css = ".product-image-container")
    RefleqtListOfWebElements productList;

    @FindBy(css = "[title=\"Add to cart\"]")
    RefleqtListOfWebElements addToCartList;

    @FindBy(css = "[title=\"Proceed to checkout\"]")
    RefleqtWebElement checkOutBtn;

    public ECommercePage clickBestSeller() {
        bestSellerSectionBtn.click();
        return this;
    }

    public ECommercePage mouseOverFirstProduct() {
        productList
                .getFirstDisplayed()
                .mouseOver();
        return this;
    }

    public void clickAddToCart() {
        addToCartList.clickFirstDisplayed();
    }

    public void validateCartPopup() {
        checkOutBtn.validatePresence(true);
    }
}
