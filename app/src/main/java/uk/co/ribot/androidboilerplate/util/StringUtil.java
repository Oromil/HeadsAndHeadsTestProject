package uk.co.ribot.androidboilerplate.util;

import uk.co.ribot.androidboilerplate.BoilerplateApplication;

/**
 * Created by Oromil on 29.11.2017.
 */

public class StringUtil {
    public static String getStringById(int stringId){
        return BoilerplateApplication.getContext().getString(stringId);
    }
}
