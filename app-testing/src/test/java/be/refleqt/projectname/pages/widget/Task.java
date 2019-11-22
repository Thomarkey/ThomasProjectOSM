package be.refleqt.projectname.pages.widget;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.Widget;
import static org.assertj.core.api.Assertions.assertThat;
import org.openqa.selenium.WebElement;

@AndroidFindBy(id = "taskList")
public class Task extends Widget {

    protected Task(WebElement element) {
        super(element);
    }

    @AndroidFindBy(id = "todoContent")
    private MobileElement contentLbl;

    @AndroidFindBy(id = "checkDone")
    private MobileElement checkBox;

    public void validateContent(String value) {
        assertThat(contentLbl.getText()).isEqualToIgnoringCase(value);
    }

    public void clickTask() {
        contentLbl.click();
    }

    public void selectCheckbox(boolean toSelect) {
        if (!Boolean.valueOf(checkBox.getAttribute("checked")).equals(toSelect)) {
            checkBox.click();
        }
    }
}
