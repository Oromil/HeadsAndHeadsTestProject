package uk.co.ribot.androidboilerplate.ui.register;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.BindView;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.injection.component.ActivityComponent;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;
import uk.co.ribot.androidboilerplate.ui.content.ContentActivity;
import uk.co.ribot.androidboilerplate.util.CheckUserDataUtil;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Oromil on 27.11.2017.
 */

public class RegisterActivity extends BaseActivity<RegisterPresenter, RegisterMvpView> implements RegisterMvpView {

    @BindView(R.id.emailInputLayout)
    TextInputLayout emailInputLayout;
    @BindView(R.id.nameTextInputLayout)
    TextInputLayout nameInputLayout;
    @BindView(R.id.passwordInputLayout)
    TextInputLayout passwordInputLayout;
    @BindView(R.id.repeatInputLayout)
    TextInputLayout repeatPassInputLayout;

    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etRepeatPassword)
    EditText etRepeatPass;
    @BindView(R.id.btnApply)
    Button btnApply;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
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

    public static void start(Context context) {
        Intent starter = new Intent(context, RegisterActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void setupViews() {
        super.setupViews();
        nameInputLayout.setVisibility(VISIBLE);
        repeatPassInputLayout.setVisibility(VISIBLE);
        btnApply.setText(R.string.sign_up);

        btnApply.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String name = etName.getText().toString();
            String password = etPassword.getText().toString();
            if (CheckUserDataUtil.isEmailValid(emailInputLayout)
                    && CheckUserDataUtil.isNameValid(nameInputLayout)
                    && CheckUserDataUtil.isPasswordValid(passwordInputLayout)
                    && CheckUserDataUtil.isTextsEquals(passwordInputLayout, repeatPassInputLayout)) {
                getPresenter().saveUser(email, name, password);
            }
        });
    }

    @Override
    protected void setupActionBar(){
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.sign_up);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
    }

    @Override
    public void navigateToContentActivity() {
        ContentActivity.start(this);
        finish();
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress(boolean show) {
        if (show)
            progressBar.setVisibility(VISIBLE);
        else progressBar.setVisibility(GONE);
    }
}
