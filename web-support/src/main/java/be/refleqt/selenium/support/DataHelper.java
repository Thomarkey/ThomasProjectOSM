package be.refleqt.selenium.support;

public class DataHelper {

    public static String getInvalid(String option) {
        switch (option) {
            case "<empty>":
                return "";
            case "<null>":
                return null;
            default:
                return option;
        }
    }
}
