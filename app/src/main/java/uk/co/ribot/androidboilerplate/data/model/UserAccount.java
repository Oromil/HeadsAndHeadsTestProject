package uk.co.ribot.androidboilerplate.data.model;

/**
 * Created by Oromil on 27.11.2017.
 */

public class UserAccount {

    public static final String EMPTY_USER_DATA = "null";

    String email;
    String password;
    String name;

    public UserAccount(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    private UserAccount() {
        this.email = EMPTY_USER_DATA;
        this.name = EMPTY_USER_DATA;
        this.password = EMPTY_USER_DATA;
    }

    public static UserAccount getEmptyUser() {
        return new UserAccount();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
