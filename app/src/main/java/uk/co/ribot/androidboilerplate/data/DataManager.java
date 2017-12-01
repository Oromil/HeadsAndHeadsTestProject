package uk.co.ribot.androidboilerplate.data;

import android.util.Log;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import uk.co.ribot.androidboilerplate.data.local.DatabaseHelper;
import uk.co.ribot.androidboilerplate.data.local.PreferencesHelper;
import uk.co.ribot.androidboilerplate.data.model.Ribot;
import uk.co.ribot.androidboilerplate.data.model.UserAccount;
import uk.co.ribot.androidboilerplate.data.model.Weather;
import uk.co.ribot.androidboilerplate.data.remote.RibotsService;
import uk.co.ribot.androidboilerplate.data.remote.WeatherService;

@Singleton
public class DataManager {

    private final RibotsService mRibotsService;
    private final DatabaseHelper mDatabaseHelper;
    private final PreferencesHelper mPreferencesHelper;
    private final WeatherService mWeatherServise;

    @Inject
    public DataManager(WeatherService weatherService, RibotsService ribotsService,
                       PreferencesHelper preferencesHelper, DatabaseHelper databaseHelper) {
        mRibotsService = ribotsService;
        mPreferencesHelper = preferencesHelper;
        mDatabaseHelper = databaseHelper;
        mWeatherServise = weatherService;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public Observable<Ribot> syncRibots() {
        return mRibotsService.getRibots()
                .concatMap(mDatabaseHelper::setRibots);
    }

    public Observable<List<Ribot>> getRibots() {
        return mDatabaseHelper.getRibots().distinct();
    }

    public Observable<UserAccount> saveUser(UserAccount user) {
        Log.d("SaveUser", "call");
        return mDatabaseHelper.addUser(user).distinct();
    }

    public Observable<UserAccount> getUser(String email) {
        return mDatabaseHelper.getUser(email).distinct();
    }

    public Observable<Weather> getWeather(float lat, float lon, String language) {
        return mWeatherServise.getWeatrher(lat, lon, language);
    }

    public Observable<Weather> getWeather(double lat, double lon) {
        return mWeatherServise.getWeatrher(lat, lon);
    }
}
