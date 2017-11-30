package uk.co.ribot.androidboilerplate.ui.signin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.injection.component.ActivityComponent;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;
import uk.co.ribot.androidboilerplate.ui.content.ContentActivity;
import uk.co.ribot.androidboilerplate.ui.register.RegisterActivity;
import uk.co.ribot.androidboilerplate.util.CheckUserDataUtil;

/**
 * Created by Oromil on 28.11.2017.
 */

public class SignInActivity extends BaseActivity<SignInPresenter, SignInMvpView> implements SignInMvpView {

    @BindView(R.id.emailInputLayout)
    TextInputLayout emailInputLayout;
    @BindView(R.id.passwordInputLayout)
    TextInputLayout passwordInputLayout;

    @BindView(R.id.etEmail)
    TextInputEditText etEmail;
    @BindView(R.id.etPassword)
    TextInputEditText etPassword;

    @BindView(R.id.btnApply)
    Button btnEnter;
    @BindView(R.id.progressBar)
    View progressBar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.user_input_layout;
    }

    @Override
    public void onCompanentCreated(@NonNull ActivityComponent component) {
        component.inject(this);
    }

    @Override
    protected void setupViews() {
        super.setupViews();
        btnEnter.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            if (CheckUserDataUtil.isEmailValid(emailInputLayout)) {
                getPresenter().signIn(email, password);
            }
        });
    }

    @Override
    protected void setupActionBar(){
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.signin);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
    }

    @Override
    public void showProgress(boolean show) {
        if (show)
            progressBar.setVisibility(View.VISIBLE);
        else progressBar.setVisibility(View.GONE);
    }

    @Override
    public void navigateToContentActivity() {
        ContentActivity.start(this);
        finish();
    }

    @Override
    public void navigateToRegisterActivity() {
        RegisterActivity.start(this);
        finish();
    }

    @Override
    public void showUserErrorDialog() {

        AlertDialog dialog = new AlertDialog.Builder(this, R.style.CustomDialog)
                    .setMessage(R.string.user_error_dialog_message)
                    .setTitle(R.string.dialog_error_title)
                    .setNegativeButton(R.string.dialog_action_cancel, (dialog1, which) -> dialog1.dismiss())
                    .setPositiveButton(R.string.sign_up, (dialog12, which) -> SignInActivity.this.navigateToRegisterActivity())
                    .create();

        dialog.show();
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, SignInActivity.class);
        context.startActivity(starter);
    }

    @Override
    public void showPasswordError() {
        CheckUserDataUtil.showError(passwordInputLayout, R.string.password_error);
    }
}
