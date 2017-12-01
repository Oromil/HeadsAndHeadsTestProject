package uk.co.ribot.androidboilerplate.ui.register;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.local.PreferencesHelper;
import uk.co.ribot.androidboilerplate.data.model.UserAccount;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.util.CheckUserDataUtil;
import uk.co.ribot.androidboilerplate.util.RxUtil;
import uk.co.ribot.androidboilerplate.util.StringUtil;

/**
 * Created by Oromil on 27.11.2017.
 */

public class RegisterPresenter extends BasePresenter<RegisterMvpView> {

    private DataManager mDataManager;
    private Disposable mDisposable;
    private PreferencesHelper mPreferencesHelper;

    @Inject
    public RegisterPresenter(DataManager dataManager) {
        mDataManager = dataManager;
        mPreferencesHelper = mDataManager.getPreferencesHelper();
    }

    public void saveUser(final String email, final String name,
                         final String pass, String passRepit) {

        if (!isDataValid(email, name, pass, passRepit)) {
            return;
        }

        getMvpView().showProgress(true);
        RxUtil.dispose(mDisposable);
        mDataManager.getUser(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .concatMap(account -> {
                    if (account.getEmail().equals(UserAccount.EMPTY_USER_DATA))
                        return mDataManager.saveUser(new UserAccount(email, name, pass));
                    else throw new Exception("can't register", new Throwable("user already exist"));
                })
                .subscribe(new Observer<UserAccount>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(UserAccount account) {
                        mPreferencesHelper.putUserData(email, pass, name);
                        onComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e.getMessage());
                        getMvpView().showToast(StringUtil.getStringById(R.string.registration_error));
                        getMvpView().showProgress(false);
                    }

                    @Override
                    public void onComplete() {
                        getMvpView().finishWithResult();
                    }
                });
    }

    private boolean isDataValid(String email, String name, String password, String passRepeat) {
        getMvpView().showEmailError(!CheckUserDataUtil.isEmailValid(email));
        getMvpView().showNameError(!CheckUserDataUtil.isNameValid(name));
        getMvpView().showPasswordError(!CheckUserDataUtil.isPasswordValid(password));
        getMvpView().showPasswordRepeatError(!passRepeat.equals(password));
        return CheckUserDataUtil.isEmailValid(email) && CheckUserDataUtil.isNameValid(name) &&
                CheckUserDataUtil.isPasswordValid(password) && passRepeat.equals(password);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mDisposable != null) mDisposable.dispose();
    }
}
