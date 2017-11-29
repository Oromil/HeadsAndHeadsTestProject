package uk.co.ribot.androidboilerplate.ui.content;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.SyncService;
import uk.co.ribot.androidboilerplate.data.model.Ribot;
import uk.co.ribot.androidboilerplate.injection.component.ActivityComponent;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;
import uk.co.ribot.androidboilerplate.util.DialogFactory;

public class ContentActivity extends BaseActivity<ContentPresenter, ContentMvpView> implements ContentMvpView {

    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "uk.co.ribot.androidboilerplate.ui.main.ContentActivity.EXTRA_TRIGGER_SYNC_FLAG";

    @Inject
    RibotsAdapter mRibotsAdapter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.mainLayout)
    ViewGroup mainLayout;

    /**
     * Return an Intent to start this Activity.
     * triggerDataSyncOnCreate allows disabling the background sync service onCreate. Should
     * only be set to false during testing.
     */
    public static Intent getStartIntent(Context context, boolean triggerDataSyncOnCreate) {
        Intent intent = new Intent(context, ContentActivity.class);
        intent.putExtra(EXTRA_TRIGGER_SYNC_FLAG, triggerDataSyncOnCreate);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupActionBar();
        mRecyclerView.setAdapter(mRibotsAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        getPresenter().loadRibots();

        if (getIntent().getBooleanExtra(EXTRA_TRIGGER_SYNC_FLAG, true)) {
            startService(SyncService.getStartIntent(this));
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_content;
    }

    @Override
    public void onCompanentCreated(@NonNull ActivityComponent component) {
        component.inject(this);
    }

    /***** MVP View methods implementation *****/

    @Override
    public void showRibots(List<Ribot> ribots) {
        mRibotsAdapter.setRibots(ribots);
        mRibotsAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError() {
        DialogFactory.createGenericErrorDialog(this, getString(R.string.error_loading_ribots))
                .show();
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(mainLayout, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("OK", view -> {
                })
                .show();
    }

    @Override
    public void showRibotsEmpty() {
        mRibotsAdapter.setRibots(Collections.<Ribot>emptyList());
        mRibotsAdapter.notifyDataSetChanged();
        Toast.makeText(this, R.string.empty_ribots, Toast.LENGTH_LONG).show();
    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_content_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_exit:
                showExitDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }

    public void showExitDialog(){
        AlertDialog dialog = new AlertDialog.Builder(this, R.style.CustomDialog)
                .setMessage(R.string.exit_dialog_message)
                .setTitle(R.string.exit_dialog_title)
                .setNegativeButton(R.string.dialog_action_cancel, (dialog1, which) -> dialog1.dismiss())
                .setPositiveButton(R.string.dialog_action_ok, (dialog12, which) -> {
                    getPresenter().clearUserData();
                    finish();
                })
                .create();

        dialog.show();
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, ContentActivity.class);
//        starter.putExtra();
        context.startActivity(starter);
    }
}
