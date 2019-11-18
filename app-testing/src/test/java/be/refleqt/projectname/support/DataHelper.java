package be.refleqt.projectname.support;

import be.refleqt.appium.support.*;

public class DataHelper extends GenericDataHelper {

    public static String getRandomString(String option) {
        switch (option) {
            case "<random>":
                return "random";
            default:
                return getInvalid(option);
        }
    }
}
