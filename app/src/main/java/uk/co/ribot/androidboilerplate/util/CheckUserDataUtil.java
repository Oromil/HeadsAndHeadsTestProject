package uk.co.ribot.androidboilerplate.util;

import android.support.design.widget.TextInputLayout;
import android.util.Patterns;

import uk.co.ribot.androidboilerplate.R;

/**
 * Created by Oromil on 27.11.2017.
 */

public class CheckUserDataUtil {

    public static final String PASSWORD_PATTERN = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}";

    public static boolean isEmailValid(TextInputLayout emailInputLayout){
            return isDataValid(emailInputLayout, Patterns.EMAIL_ADDRESS.pattern(), R.string.email_error);
    }

    public static boolean isPasswordValid(TextInputLayout passwordInputLayout){
        return isDataValid(passwordInputLayout, PASSWORD_PATTERN, R.string.password_error);
    }

    public static boolean isTextsEquals(TextInputLayout inputLayout1, TextInputLayout inputLayout2){
        String text1 = inputLayout1.getEditText().getText().toString();
        String text2 = inputLayout2.getEditText().getText().toString();
        if (text1.equals(text2)){
            return true;
        }else {
            showError(inputLayout2, R.string.repeat_password_error);
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
        return isDataValid(layout, pattern, R.string.input_data_error);
    }

    private static boolean isDataValid(TextInputLayout layout, String pattern, int msgErrorId){
        String text = layout.getEditText().getText().toString();
        if (text.matches(pattern)){
            layout.setErrorEnabled(false);
            return true;
        }else if (!isTextEmpty(layout)){
            showError(layout, msgErrorId);
        }
        return false;
    }

    private static boolean isTextEmpty(TextInputLayout layout){
        if (layout.getEditText().getText().toString().isEmpty()){
            showError(layout, R.string.text_input_empty_error);
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

    public static void showError(TextInputLayout layout, int msgErrorId){
        showError(layout, StringUtil.getStringById(msgErrorId));
    }
}
