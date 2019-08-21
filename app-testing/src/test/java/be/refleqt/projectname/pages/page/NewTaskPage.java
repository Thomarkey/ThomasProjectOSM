package be.refleqt.projectname.pages.page;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class NewTaskPage extends AbstractPage {

    @AndroidFindBy(id = "todoEditText")
    MobileElement taskTxtFld;

    @AndroidFindBy(id = "addTodoBtn")
    MobileElement confirmTaskBtn;

    public NewTaskPage enterTask(String task) {
        waitForVisibility(taskTxtFld);
        taskTxtFld.clear();
        taskTxtFld.sendKeys(task);
        return this;
    }

    public void clickConfirm() {
        clickOnElement(confirmTaskBtn);
    }
}
