package uk.co.ribot.androidboilerplate.ui.main;

import java.util.Map;

import javax.inject.Inject;

import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.local.PreferencesHelper;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;

/**
 * Created by Oromil on 27.11.2017.
 */

public class MainPresenter extends BasePresenter<MainMvpView> {

    private PreferencesHelper mPreferencesHelper;

    @Inject
    public MainPresenter(DataManager dataManager) {
        mPreferencesHelper = dataManager.getPreferencesHelper();
    }

    @Override
    protected void onViewAttached() {
        super.onViewAttached();
        if (hasEnteredUser()) {
            getMvpView().navigateToContentActivity();
        }
    }

    public boolean hasEnteredUser() {
        Map<String, String> userData = mPreferencesHelper.getUserData();
        String userEmail = userData.get(PreferencesHelper.USER_EMAIL);
        return userEmail != null;
    }
}
