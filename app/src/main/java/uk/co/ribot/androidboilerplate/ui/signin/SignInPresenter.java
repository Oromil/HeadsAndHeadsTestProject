package uk.co.ribot.androidboilerplate.ui.signin;

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
 * Created by Oromil on 28.11.2017.
 */

public class SignInPresenter extends BasePresenter<SignInMvpView> {

    private DataManager mDataManager;
    private PreferencesHelper mPreferencesHelper;
    private RxEventBus mEventBus;
    private Disposable mDisposable;

    @Inject
    public SignInPresenter(DataManager dataManager, RxEventBus eventBus) {
        mEventBus = eventBus;
        mDataManager = dataManager;
        mPreferencesHelper = mDataManager.getPreferencesHelper();
    }

    public void signIn(final String email, final String password) {

        RxUtil.dispose(mDisposable);
        mDataManager.getUser(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserAccount>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(UserAccount account) {
                        if (!account.getEmail().equals("null")) {
                            if (account.getPassword().equals(password)) {
                                mPreferencesHelper.putUserData(account.getEmail(),
                                        account.getPassword(), account.getName());
                                mEventBus.signInEvent(account.getName());
                                onComplete();
                            } else {
                                getMvpView().showPasswordError();
                                onError(new Throwable(""));
                            }
                        } else {
                            getMvpView().showUserErrorDialog();
                            onError(new Throwable(""));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
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
