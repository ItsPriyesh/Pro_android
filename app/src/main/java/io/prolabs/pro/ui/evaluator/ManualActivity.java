package io.prolabs.pro.ui.evaluator;

import android.os.Bundle;

import io.prolabs.pro.R;
import io.prolabs.pro.ui.common.BaseToolBarActivity;

public class ManualActivity extends BaseToolBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);
        setToolbarTitle("Job Description");
        showToolbarBackButton();


    }
}
