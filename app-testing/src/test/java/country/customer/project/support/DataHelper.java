package country.customer.project.support;

import country.customer.project.appium.support.GenericDataHelper;

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
