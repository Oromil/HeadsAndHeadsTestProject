package uk.co.ribot.androidboilerplate.ui.register;

import android.util.Log;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.local.PreferencesHelper;
import uk.co.ribot.androidboilerplate.data.model.UserAccount;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.util.RxEventBus;
import uk.co.ribot.androidboilerplate.util.RxUtil;

/**
 * Created by Oromil on 27.11.2017.
 */

public class RegisterPresenter extends BasePresenter<RegisterMvpView> {

    private DataManager mDataManager;
    private Disposable mDisposable;
    private PreferencesHelper mPreferencesHelper;
    private RxEventBus mEventBus;

    @Inject
    public RegisterPresenter(DataManager dataManager, RxEventBus eventBus) {
        mDataManager = dataManager;
        mPreferencesHelper = mDataManager.getPreferencesHelper();
        mEventBus = eventBus;
    }

    public void saveUser(final String email, final String name, final String pass) {
        getMvpView().showProgress(true);
        RxUtil.dispose(mDisposable);

        mDataManager.getUser(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .concatMap(account -> {
                    if (account.getEmail().equals("null"))
                        return mDataManager.saveUser(new UserAccount(email, name, pass));
                    else throw new Exception("error");
                })
                .subscribe(new Observer<UserAccount>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(UserAccount account) {
                        mPreferencesHelper.putUserData(email, pass, name);
                        mEventBus.signInEvent(name);
                        onComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        getMvpView().showToast("User is already registered");
                        getMvpView().showProgress(false);
                    }

                    @Override
                    public void onComplete() {
                        getMvpView().showProgress(false);
                        getMvpView().navigateToContentActivity();
                    }
                });
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mDisposable != null) mDisposable.dispose();
    }

}
