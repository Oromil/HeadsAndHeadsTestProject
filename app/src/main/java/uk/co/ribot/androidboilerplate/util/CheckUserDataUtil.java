package uk.co.ribot.androidboilerplate.util;

import android.support.design.widget.TextInputLayout;
import android.util.Patterns;

/**
 * Created by Oromil on 27.11.2017.
 */

public class CheckUserDataUtil {

    public static final String PASSWORD_PATTERN = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}";

    public static boolean isEmailValid(TextInputLayout emailInputLayout){
            return isDataValid(emailInputLayout, Patterns.EMAIL_ADDRESS.pattern());
    }

    public static boolean isPasswordValid(TextInputLayout passwordInputLayout){
        return isDataValid(passwordInputLayout, PASSWORD_PATTERN);
    }

    public static boolean isTextsEquals(TextInputLayout inputLayout1, TextInputLayout inputLayout2){
        String text1 = inputLayout1.getEditText().getText().toString();
        String text2 = inputLayout2.getEditText().getText().toString();
        if (text1.equals(text2)){
            return true;
        }else {
            showError(inputLayout2, "texts must equals");
            return false;
        }
    }

    public static boolean isNameValid(TextInputLayout nameInputLayout){
        if (isTextEmpty(nameInputLayout)){
            return false;
        }else {
            nameInputLayout.setErrorEnabled(false);
            return true;
        }
    }

    private static boolean isDataValid(TextInputLayout layout, String pattern){
        String text = layout.getEditText().getText().toString();
        if (text.matches(pattern)){
            layout.setErrorEnabled(false);
            return true;
        }else if (!isTextEmpty(layout)){
            showError(layout, "incorrect data");
        }
        return false;
    }

    private static boolean isTextEmpty(TextInputLayout layout){
        if (layout.getEditText().getText().toString().isEmpty()){
            showError(layout, "empty");
            return true;
        }else{
            return false;
        }
    }

    public static void showError(TextInputLayout layout, String msgError){
        layout.setError(msgError);
        layout.setErrorEnabled(true);
        layout.getEditText().requestFocus();
    }
}
