package be.refleqt.projectname.pages.page;

import be.refleqt.projectname.pages.widget.Task;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import java.util.List;

public class HomePage extends AbstractPage {

    @AndroidFindBy(id = "actionButton")
    private MobileElement newTaskBtn;

    private List<Task> taskList;

    public NewTaskPage clickCreateNewTask() {
        clickOnElement(newTaskBtn);
        return new NewTaskPage();
    }

    public void checkCheckbox(boolean status, int index) {
        waitForVisibility(newTaskBtn);
        waitForVisibility(taskList);
        taskList.get(index).selectCheckbox(status);
    }

    public DetailPage selectLastTask() {
        waitForVisibility(taskList);
        taskList.get(taskList.size() - 1).clickTask();

        return new DetailPage();
    }

    public void checkAllCheckboxes(boolean status) {
        waitForVisibility(newTaskBtn);
        waitForVisibility(taskList);
        taskList.forEach(t -> t.selectCheckbox(status));
    }
}
