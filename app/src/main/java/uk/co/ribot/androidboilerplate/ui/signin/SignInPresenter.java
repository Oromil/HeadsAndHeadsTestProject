package uk.co.ribot.androidboilerplate.ui.signin;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.local.PreferencesHelper;
import uk.co.ribot.androidboilerplate.data.model.UserAccount;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.util.CheckUserDataUtil;
import uk.co.ribot.androidboilerplate.util.RxUtil;

/**
 * Created by Oromil on 28.11.2017.
 */

public class SignInPresenter extends BasePresenter<SignInMvpView> {

    private DataManager mDataManager;
    private PreferencesHelper mPreferencesHelper;
    private Disposable mDisposable;

    @Inject
    public SignInPresenter(DataManager dataManager) {
        mDataManager = dataManager;
        mPreferencesHelper = mDataManager.getPreferencesHelper();
    }

    public void signIn(final String email, final String password) {

        if (!CheckUserDataUtil.isEmailValid(email)) {
            getMvpView().showEmailError(true);
            return;
        } else getMvpView().showEmailError(false);

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
                        if (!account.getEmail().equals(UserAccount.EMPTY_USER_DATA)) {
                            if (account.getPassword().equals(password)) {
                                mPreferencesHelper.putUserData(account.getEmail(),
                                        account.getPassword(), account.getName());
                                getMvpView().navigateToContentActivity();
                                onComplete();
                            } else {
                                getMvpView().showPasswordError(true);
                                getMvpView().showProgress(false);
                            }
                        } else {
                            getMvpView().showUserErrorDialog(email);
                            onError(new Throwable("failed login",
                                    new Throwable("user doesn't exist")));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e.getMessage());
                        mDisposable.dispose();
                        getMvpView().showProgress(false);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mDisposable != null) mDisposable.dispose();
    }
}
