package io.prolabs.pro;

import android.app.Application;

import timber.log.Timber;

public class ProApp extends Application {

    public static final String AUTH_KEY = "AUTH_KEY";

    @Override
    public void onCreate() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
