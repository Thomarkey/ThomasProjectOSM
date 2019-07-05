package be.refleqt.projectname.utils;

import org.apache.commons.lang.RandomStringUtils;

public class DataHelper {

    public static String getGeneric(String option) {
        switch (option) {
            case "<randomString>":
                return RandomStringUtils.randomNumeric(7);
            case "<empty>":
                return "";
            case "<null>":
                return null;
            default:
                return option;
        }
    }
}
