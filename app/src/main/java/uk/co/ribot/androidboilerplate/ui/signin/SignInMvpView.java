package uk.co.ribot.androidboilerplate.ui.signin;

import uk.co.ribot.androidboilerplate.ui.base.MvpView;

/**
 * Created by Oromil on 28.11.2017.
 */

public interface SignInMvpView extends MvpView {
    void showProgress(boolean show);

    void navigateToContentActivity();

    void navigateToRegisterActivity();

    void showUserErrorDialog();

    void showPasswordError();
}
