package uk.co.ribot.androidboilerplate.ui.main;

import android.location.LocationManager;

import uk.co.ribot.androidboilerplate.ui.base.MvpView;

/**
 * Created by Oromil on 27.11.2017.
 */

public interface MainMvpView extends MvpView {
    LocationManager getLocationManager();

    void requestPermissions();

    void navigateToContentActivity();
}
