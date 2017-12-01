package uk.co.ribot.androidboilerplate.ui.signin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.injection.component.ActivityComponent;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;
import uk.co.ribot.androidboilerplate.ui.content.ContentActivity;
import uk.co.ribot.androidboilerplate.ui.register.RegisterActivity;

/**
 * Created by Oromil on 28.11.2017.
 */

public class SignInActivity extends BaseActivity<SignInPresenter, SignInMvpView>
        implements SignInMvpView {

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

    public static void start(Context context) {
        Intent starter = new Intent(context, SignInActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.user_input_layout;
    }

    @Override
    public void onCompanentCreated(@NonNull ActivityComponent component) {
        component.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showProgress(false);
    }

    @Override
    protected void setupViews() {
        super.setupViews();
        btnEnter.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            getPresenter().signIn(email, password);
        });
    }

    @Override
    protected void setupActionBar() {
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.authorization);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_signin_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_register:
                navigateToRegisterActivity();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showProgress(boolean show) {
        if (show)
            progressBar.setVisibility(View.VISIBLE);
        else progressBar.setVisibility(View.GONE);
    }

    @Override
    public void navigateToContentActivity() {
        showProgress(true);
        ContentActivity.start(this);
        finish();
    }

    @Override
    public void navigateToRegisterActivity() {
        navigateToRegisterActivity(null);
    }

    private void navigateToRegisterActivity(String email) {
        showProgress(true);

        Intent starter = new Intent(this, RegisterActivity.class);
        if (email != null) {
            starter.putExtra(RegisterActivity.USER_EMAIL, email);
        }
        startActivityForResult(starter, RegisterActivity.REQUEST_USER_CODE);
    }

    @Override
    public void showUserErrorDialog(String email) {

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(R.string.user_error_dialog_message)
                .setTitle(R.string.dialog_error_title)
                .setNegativeButton(R.string.dialog_action_cancel, (dialog1, which) -> dialog1.dismiss())
                .setPositiveButton(R.string.sign_up, (dialog12, which) -> SignInActivity.this
                        .navigateToRegisterActivity(email))
                .create();

        dialog.show();
    }

    @Override
    public void showPasswordError(boolean show) {
        showTextInputLayoutError(passwordInputLayout, show, R.string.password_error);
    }

    @Override
    public void showEmailError(boolean show) {
        showTextInputLayoutError(emailInputLayout, show, R.string.email_error);
    }

    private void showTextInputLayoutError(TextInputLayout layout, boolean show, int msgErrorId) {
        layout.setError(getString(msgErrorId));
        layout.setErrorEnabled(show);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RegisterActivity.REQUEST_USER_CODE && resultCode == RESULT_OK) {
            navigateToContentActivity();
            finish();
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }
}
