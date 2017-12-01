package uk.co.ribot.androidboilerplate.ui.register;

import android.content.Intent;
import android.os.Bundle;
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

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Oromil on 27.11.2017.
 */

public class RegisterActivity extends BaseActivity<RegisterPresenter, RegisterMvpView> implements RegisterMvpView {

    public static final int REQUEST_USER_CODE = 1;
    public static final String USER_DATA = "user_data";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent startIntent = getIntent();
        if (startIntent != null) {
            String userEmail = startIntent.getStringExtra("email");
            if (userEmail != null) {
                etEmail.setText(userEmail);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showProgress(false);
    }

    @Override
    protected void setupViews() {
        super.setupViews();
        nameInputLayout.setVisibility(VISIBLE);
        repeatPassInputLayout.setVisibility(VISIBLE);
        btnApply.setText(R.string.sign_up);

        btnApply.setOnClickListener(v -> {
            getPresenter().saveUser(getTextFromEditText(etEmail), getTextFromEditText(etName),
                    getTextFromEditText(etPassword), getTextFromEditText(etRepeatPass));
        });
    }

    private String getTextFromEditText(EditText editText) {
        return editText.getText().toString();
    }

    @Override
    protected void setupActionBar() {
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.registration);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
    }

    @Override
    public void finishWithResult() {
        setResult(RESULT_OK);
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

    @Override
    public void showEmailError(boolean show) {
        showTextInpuLayoutError(emailInputLayout, show, R.string.email_error);
    }

    @Override
    public void showNameError(boolean show) {
        showTextInpuLayoutError(nameInputLayout, show, R.string.name_error);
    }

    @Override
    public void showPasswordError(boolean show) {
        showTextInpuLayoutError(passwordInputLayout, show, R.string.password_error);

    }

    @Override
    public void showPasswordRepeatError(boolean show) {
        showTextInpuLayoutError(repeatPassInputLayout, show, R.string.repeat_password_error);
    }

    private void showTextInpuLayoutError(TextInputLayout layout, boolean show, int msgErrorId) {
        layout.setError(getString(msgErrorId));
        layout.setErrorEnabled(show);
    }
}
