package uk.co.ribot.androidboilerplate.ui.register;

import uk.co.ribot.androidboilerplate.ui.base.MvpView;

/**
 * Created by Oromil on 27.11.2017.
 */

public interface RegisterMvpView extends MvpView {

    void navigateToContentActivity();

    void showToast(String text);

    void showProgress(boolean show);
}
