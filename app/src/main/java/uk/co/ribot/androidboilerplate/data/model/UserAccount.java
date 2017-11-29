package uk.co.ribot.androidboilerplate.data.model;

/**
 * Created by Oromil on 27.11.2017.
 */

public class UserAccount {

    String email;
    String password;
    String name;

    public UserAccount(String email, String name, String password){
        this.email = email;
        this.name = name;
        this.password = password;
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
