package country.customer.project.appium.support;

public class GenericDataHelper {

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

    public static Boolean getBooleanValue(Object option) {
        if (option == null)
            return false;

        switch (option.toString().toLowerCase()) {
            case "true":
                return true;
            case "false":
            default:
                return false;
        }
    }
}
