package uk.co.ribot.androidboilerplate.util;

import android.util.Patterns;

/**
 * Created by Oromil on 27.11.2017.
 */

public class CheckUserDataUtil {

    public static final String PASSWORD_PATTERN = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}";

    private static boolean isDataValid(String data, String pattern) {
        if (data.matches(pattern))
            return true;
        else
            return false;
    }

    public static boolean isEmailValid(String email) {
        return isDataValid(email, Patterns.EMAIL_ADDRESS.pattern());
    }

    public static boolean isNameValid(String name) {
        return !name.equals("");
    }

    public static boolean isPasswordValid(String password) {
        return isDataValid(password, PASSWORD_PATTERN);
    }
}
