package io.prolabs.pro.ui.settings;

import android.os.Bundle;

import io.prolabs.pro.R;
import io.prolabs.pro.ui.common.BaseToolBarActivity;

public class SettingsActivity extends BaseToolBarActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setToolbarTitle("Settings");
        showToolbarBackButton();

        getFragmentManager().beginTransaction()
                .add(R.id.container, new SettingsFragment())
                .commit();
    }
}