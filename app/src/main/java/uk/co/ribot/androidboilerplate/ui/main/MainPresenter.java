package uk.co.ribot.androidboilerplate.ui.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import java.util.Map;

import javax.inject.Inject;

import uk.co.ribot.androidboilerplate.BoilerplateApplication;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.local.PreferencesHelper;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.util.RxEventBus;

/**
 * Created by Oromil on 27.11.2017.
 */

public class MainPresenter extends BasePresenter<MainMvpView> {

    public static final double MOSCOW_LATITUDE = 55.75d;
    public static final double MOSCOW_LONGITUDE = 37.61d;

    private PreferencesHelper mPreferencesHelper;
    private LocationManager mLocationManager;
    private RxEventBus mEventBus;
    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mEventBus.postLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    };

    @Inject
    public MainPresenter(DataManager dataManager, RxEventBus eventBus) {
        mPreferencesHelper = dataManager.getPreferencesHelper();
        mEventBus = eventBus;
    }

    @Override
    protected void onViewAttached() {
        super.onViewAttached();
        mLocationManager = getMvpView().getLocationManager();
        requestCurrentLocation();
    }

    public void requestCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(BoilerplateApplication.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(BoilerplateApplication.getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            getMvpView().requestPermissions();
            return;
        }

        Location lastLocation = mLocationManager
                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (lastLocation != null) {
            mEventBus.postLocation(lastLocation);
        } else {
            setMockLocation();
        }
        mLocationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER,
                mLocationListener, null);
        checkEnteredUserAndStart();
    }

    public void setMockLocation() {
        Location mockLocation = new Location("");
        mockLocation.setLatitude(MOSCOW_LATITUDE);
        mockLocation.setLongitude(MOSCOW_LONGITUDE);
        mEventBus.postLocation(mockLocation);
    }

    public void checkEnteredUserAndStart() {
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
