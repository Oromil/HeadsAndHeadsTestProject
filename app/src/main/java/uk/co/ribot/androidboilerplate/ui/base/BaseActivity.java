package uk.co.ribot.androidboilerplate.ui.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.util.LongSparseArray;
import android.support.v7.app.AppCompatActivity;

import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Inject;

import butterknife.ButterKnife;
import timber.log.Timber;
import uk.co.ribot.androidboilerplate.BoilerplateApplication;
import uk.co.ribot.androidboilerplate.injection.component.ActivityComponent;
import uk.co.ribot.androidboilerplate.injection.component.ConfigPersistentComponent;
import uk.co.ribot.androidboilerplate.injection.component.DaggerConfigPersistentComponent;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;

/**
 * Abstract activity that every other Activity in this application must implement. It handles
 * creation of Dagger components and makes sure that instances of ConfigPersistentComponent survive
 * across configuration changes.
 */
public abstract class BaseActivity<P extends Presenter, V extends MvpView> extends AppCompatActivity {

    private static final String KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID";
    private static final AtomicLong NEXT_ID = new AtomicLong(0);
    private static final LongSparseArray<ConfigPersistentComponent>
            sComponentsMap = new LongSparseArray<>();
    @Inject
    P mPresenter;
    private ActivityComponent mActivityComponent;
    private long mActivityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the ActivityComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.
        mActivityId = savedInstanceState != null ?
                savedInstanceState.getLong(KEY_ACTIVITY_ID) : NEXT_ID.getAndIncrement();

        ConfigPersistentComponent configPersistentComponent = sComponentsMap.get(mActivityId, null);

        if (configPersistentComponent == null) {
            Timber.i("Creating new ConfigPersistentComponent id=%d", mActivityId);
            configPersistentComponent = DaggerConfigPersistentComponent.builder()
                    .applicationComponent(BoilerplateApplication.get(this).getComponent())
                    .build();
            sComponentsMap.put(mActivityId, configPersistentComponent);
        }
        mActivityComponent = configPersistentComponent.activityComponent(new ActivityModule(this));

        onCompanentCreated(mActivityComponent);
        setContentView();
        ButterKnife.bind(this);
        setupViews();
        mPresenter.attachView((V) this);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_ACTIVITY_ID, mActivityId);
    }

    @Override
    protected void onDestroy() {
        if (!isChangingConfigurations()) {
            Timber.i("Clearing ConfigPersistentComponent id=%d", mActivityId);
            sComponentsMap.remove(mActivityId);
        }
        super.onDestroy();

        mPresenter.detachView();
    }

    protected void setupViews() {
        setupActionBar();
    }

    protected void setupActionBar() {
    }

    public ActivityComponent activityComponent() {
        return mActivityComponent;
    }

    public void setContentView() {
        super.setContentView(getLayoutId());
    }

    protected abstract int getLayoutId();

    public abstract void onCompanentCreated(@NonNull ActivityComponent component);

    protected P getPresenter() {
        return mPresenter;
    }
}
