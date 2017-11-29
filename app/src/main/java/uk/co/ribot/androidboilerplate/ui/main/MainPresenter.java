package uk.co.ribot.androidboilerplate.ui.main;

import java.util.Map;

import javax.inject.Inject;

import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.local.PreferencesHelper;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.util.RxEventBus;

/**
 * Created by Oromil on 27.11.2017.
 */

public class MainPresenter extends BasePresenter<MainMvpView> {

    private DataManager mDataManager;
    private PreferencesHelper mPreferencesHelper;
    private RxEventBus mEventBus;

    @Inject
    public MainPresenter(DataManager dataManager, RxEventBus eventBus) {
        mEventBus = eventBus;
        mDataManager = dataManager;
        mPreferencesHelper = mDataManager.getPreferencesHelper();
    }

    public boolean hasEnteredUser() {
        Map<String, String> userData = mPreferencesHelper.getUserData();
        String userEmail = userData.get(PreferencesHelper.USER_EMAIL);
        if (userEmail != null) {
            mEventBus.signInEvent(userData.get(PreferencesHelper.USER_NAME));
            return true;
        }
        return false;
    }
}
