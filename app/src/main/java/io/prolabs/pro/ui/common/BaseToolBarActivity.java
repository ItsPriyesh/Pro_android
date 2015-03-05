package io.prolabs.pro.ui.common;

import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.prolabs.pro.R;

public class BaseToolBarActivity extends ActionBarActivity {

    @InjectView(R.id.toolbar)
    protected Toolbar toolbar;

    @InjectView(R.id.toolbarTitle)
    protected TextView toolbarTitle;

    private ActionBar actionBar;

    @Override
    public void setContentView(int layoutResId) {
        super.setContentView(layoutResId);
        ButterKnife.inject(this);

        toolbarTitle.setTypeface(Typeface.createFromAsset(getAssets(), "ZonaPro-Bold.otf"));

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void setToolbarTitle(String title) {
        toolbarTitle.setText(title);
    }

    protected void disableToolbarElevation() {
        toolbar.setElevation(0);
    }

    protected void showToolbarBackButton() {
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
