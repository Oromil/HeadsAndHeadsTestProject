package uk.co.ribot.androidboilerplate.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import uk.co.ribot.androidboilerplate.injection.ApplicationContext;

@Singleton
public class PreferencesHelper {

    public static final String PREF_FILE_NAME = "android_boilerplate_pref_file";

    public static final String USER_EMAIL = "email";
    public static final String USER_PASSWORD = "password";
    public static final String USER_NAME = "name";

    private final SharedPreferences mPref;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void putUserData(String email, String password, String name) {
        mPref.edit().putString(USER_EMAIL, email).apply();
        mPref.edit().putString(USER_PASSWORD, password).apply();
        mPref.edit().putString(USER_NAME, name).apply();
    }

    public Map<String, String> getUserData() {
        Map<String, String> userData = new HashMap<>();
        userData.put(USER_EMAIL, mPref.getString(USER_EMAIL, null));
        userData.put(USER_PASSWORD, mPref.getString(USER_PASSWORD, null));
        userData.put(USER_NAME, mPref.getString(USER_NAME, null));
        return userData;
    }

    public void clear() {
        mPref.edit().clear().apply();
    }

}
