package be.refleqt.projectname.pages.page;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;

public class DetailPage extends AbstractPage {

    @AndroidFindBy(id = "editTodoBtn")
    MobileElement editBtn;

    @iOSXCUITFindBy(id = "")
    @AndroidFindBy(id = "deleteTodoBtn")
    MobileElement deleteBtn;

    public NewTaskPage clickEdit() {
        clickOnElement(editBtn);
        return new NewTaskPage();
    }

    public void clickDelete() {
        clickOnElement(deleteBtn);
    }
}
