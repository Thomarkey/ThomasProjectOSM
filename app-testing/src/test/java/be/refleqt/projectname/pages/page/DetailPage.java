package be.refleqt.projectname.pages.page;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class DetailPage extends AbstractPage {

    @AndroidFindBy(id = "editTodoBtn")
    MobileElement editBtn;

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
