package io.prolabs.pro;

import android.app.Application;

import timber.log.Timber;

public class ProApp extends Application {

    // Keys used to access the paired value in shared prefs
    public static final String GITHUB_AUTH_KEY = "GITHUB_AUTH_KEY";
    public static final String LINKEDIN_AUTH_KEY = "LINKEDIN_AUTH_KEY";
    public static final String GITHUB_USER = "GITHUB_USER";

    @Override
    public void onCreate() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
