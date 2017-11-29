package uk.co.ribot.androidboilerplate.ui.signin;

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

/**
 * Created by Oromil on 28.11.2017.
 */

public class SignInPresenter extends BasePresenter<SignInMvpView> {

    private static final String TAG = "SignIn";
    private DataManager mDataManager;
    private PreferencesHelper mPreferencesHelper;
    private RxEventBus mEventBus;

    @Inject
    public SignInPresenter(DataManager dataManager, RxEventBus eventBus){
        mEventBus = eventBus;
        mDataManager = dataManager;
        mPreferencesHelper = mDataManager.getPreferencesHelper();
    }

    public void signIn(final String email, final String password){

        mDataManager.getUser(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserAccount>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(UserAccount account) {
                        Log.d(TAG, "onNext");
                        if (!account.getEmail().equals("null")){
                            if (account.getPassword().equals(password)){
                                mPreferencesHelper.putUserData(account.getEmail(),
                                        account.getPassword(), account.getName());
                                mEventBus.signInEvent(account.getName());
                                onComplete();
                            }else {
                                onError(new Throwable(""));
                            }
                        }else {
                            getMvpView().showUserErrorDialog();
                            onError(new Throwable(""));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG,"onComplete");
                        getMvpView().navigateToContentActivity();
                    }
                });
    }
}
