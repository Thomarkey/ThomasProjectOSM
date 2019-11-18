package be.refleqt.projectname.support;

import static be.refleqt.selenium.support.DataHelper.*;

/**
 *  DataHelper that imports a Generic DataHelper.
 *  Also includes an example of how you could use it.
 *
 *  Generic DataHelper contains getInvalid
 *      -> <null> returns null
 *      -> <empty> returns ""
 *      -> Default returns the given value
 */
public class DataHelper {

    public static String getSearchResult(String option) {
        switch (option) {
            case "<option>":
                return "Something I expect here";
            default:
                return getInvalid(option);
        }
    }
}
