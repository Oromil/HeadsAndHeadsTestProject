package uk.co.ribot.androidboilerplate.ui.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.injection.component.ActivityComponent;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;
import uk.co.ribot.androidboilerplate.ui.content.ContentActivity;
import uk.co.ribot.androidboilerplate.ui.signin.SignInActivity;

/**
 * Created by Oromil on 27.11.2017.
 */

public class MainActivity extends BaseActivity<MainPresenter, MainMvpView> implements MainMvpView {

    public static final int LOCATION_PERMISSIONS_REQUEST_CODE = 3;

    @BindView(R.id.btnEnter)
    Button btnEnter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progressBar)
    View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public LocationManager getLocationManager() {
        return (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    @Override
    public void requestPermissions() {
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION};
        ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSIONS_REQUEST_CODE);
    }

    @Override
    protected void setupViews() {
        super.setupViews();
        btnEnter.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            SignInActivity.start(v.getContext());
        });
    }

    @Override
    protected void setupActionBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(R.string.app_name);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onCompanentCreated(@NonNull ActivityComponent component) {
        component.inject(this);
    }

    @Override
    public void navigateToContentActivity() {
        ContentActivity.start(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getPresenter().requestCurrentLocation();
                } else {
                    getPresenter().setMockLocation();
                    getPresenter().checkEnteredUserAndStart();
                }
            }
        }
    }
}
