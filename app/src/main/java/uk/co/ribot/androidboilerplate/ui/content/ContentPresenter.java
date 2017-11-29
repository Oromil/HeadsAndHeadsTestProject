package uk.co.ribot.androidboilerplate.ui.content;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;
import uk.co.ribot.androidboilerplate.BoilerplateApplication;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.local.PreferencesHelper;
import uk.co.ribot.androidboilerplate.data.model.Ribot;
import uk.co.ribot.androidboilerplate.data.model.Weather;
import uk.co.ribot.androidboilerplate.injection.ConfigPersistent;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.util.RxEventBus;
import uk.co.ribot.androidboilerplate.util.RxUtil;

@ConfigPersistent
public class ContentPresenter extends BasePresenter<ContentMvpView> {

    private final DataManager mDataManager;
    private Disposable mRibotDisposable = null;
    private Disposable mWeatherDisposable = null;
    ArrayList<Disposable> mDisposables;
    private PreferencesHelper mPreferencesHelper;
    private RxEventBus mEventBus;

    @Inject
    public ContentPresenter(DataManager dataManager, RxEventBus eventBus) {
        mDataManager = dataManager;
        mEventBus = eventBus;
        mPreferencesHelper = mDataManager.getPreferencesHelper();
        mDisposables = new ArrayList<>();
    }

    @Override
    public void attachView(ContentMvpView mvpView) {
        super.attachView(mvpView);
        onViewAttached();
    }

    private void onViewAttached() {
        final String[] name = new String[1];
        RxUtil.dispose(mWeatherDisposable);
        mWeatherDisposable = mEventBus.filteredObservable(String.class)
                .subscribeOn(Schedulers.io())
                .flatMap(s -> {
                    name[0] = s;
                    return mDataManager.getWeather(35, 139, Locale.getDefault().getLanguage())
                            .toFlowable(BackpressureStrategy.BUFFER);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(weather -> {
                    Map<String, String> weatherMap = weather.getWeather();

                    ContentPresenter.this.getMvpView().showSnackBar(String.format(BoilerplateApplication.getContext()
                                    .getResources().getString(R.string.weather_message), name[0],
                            weatherMap.get(Weather.CITY), weatherMap.get(Weather.TEMP), weatherMap.get(Weather.DESCRIPTION)));
                });
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mRibotDisposable != null) mRibotDisposable.dispose();
        if (mWeatherDisposable != null) mWeatherDisposable.dispose();
    }

    public void loadRibots() {
        checkViewAttached();
        RxUtil.dispose(mRibotDisposable);
        mDataManager.getRibots()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<Ribot>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mRibotDisposable = d;
                    }

                    @Override
                    public void onNext(@NonNull List<Ribot> ribots) {
                        if (ribots.isEmpty()) {
                            getMvpView().showRibotsEmpty();
                        } else {
                            getMvpView().showRibots(ribots);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e(e, "There was an error loading the ribots.");
                        getMvpView().showError();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void clearUserData() {
        mPreferencesHelper.clear();
    }

}
