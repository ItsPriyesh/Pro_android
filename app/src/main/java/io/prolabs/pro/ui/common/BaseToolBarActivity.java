package io.prolabs.pro.ui.common;

import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.prolabs.pro.R;

public class BaseToolBarActivity extends ActionBarActivity {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.toolbarTitle)
    TextView toolbarTitle;

    @Override
    public void setContentView(int layoutResId) {
        super.setContentView(layoutResId);
        ButterKnife.inject(this);

        toolbarTitle.setTypeface(Typeface.createFromAsset(getAssets(), "ZonaPro-Bold.otf"));

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
    }

    protected void setToolbarTitle(String title) {
        toolbarTitle.setText(title);
    }
}
