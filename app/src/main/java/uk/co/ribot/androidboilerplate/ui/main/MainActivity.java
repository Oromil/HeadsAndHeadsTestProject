package uk.co.ribot.androidboilerplate.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;

import butterknife.BindView;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.injection.component.ActivityComponent;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;
import uk.co.ribot.androidboilerplate.ui.content.ContentActivity;
import uk.co.ribot.androidboilerplate.ui.register.RegisterActivity;
import uk.co.ribot.androidboilerplate.ui.signin.SignInActivity;

/**
 * Created by Oromil on 27.11.2017.
 */

public class MainActivity extends BaseActivity<MainPresenter, MainMvpView> implements MainMvpView{

    @BindView(R.id.btnEnter)
    Button btnEnter;
    @BindView(R.id.btnRegister)
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getPresenter().hasEnteredUser()){
            ContentActivity.start(this);
        }

        btnRegister.setOnClickListener(v -> RegisterActivity.start(v.getContext()));

        btnEnter.setOnClickListener(v -> SignInActivity.start(v.getContext()));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onCompanentCreated(@NonNull ActivityComponent component) {
        component.inject(this);
    }
}
