package io.prolabs.pro.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.orhanobut.hawk.Hawk;

import butterknife.InjectView;
import butterknife.OnClick;
import io.prolabs.pro.ProApp;
import io.prolabs.pro.R;
import io.prolabs.pro.api.GitHubApi;
import io.prolabs.pro.ui.common.BaseToolBarActivity;
import io.prolabs.pro.ui.login.LoginFragment;
import io.prolabs.pro.ui.profile.ProfileActivity;
import io.prolabs.pro.utils.NetworkUtils;
import io.prolabs.pro.utils.ViewUtils;

public class MainActivity extends BaseToolBarActivity {

    @InjectView(R.id.splashContainer)
    View splashContainer;

    @InjectView(R.id.networkErrorContainer)
    View networkErrorContainer;

    @InjectView(R.id.loginPrompt)
    View loginPrompt;

    private boolean authExists = false;

    final Handler handler = new Handler();
    final Runnable authCheckComplete = () -> onAuthCheckComplete();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbarTitle("Pro");

        checkForInternet();
    }

    private void checkForInternet() {
        if (ViewUtils.isVisible(networkErrorContainer)) ViewUtils.hide(networkErrorContainer);

        if (NetworkUtils.isConnectedToInternet(this)) {
            ViewUtils.show(splashContainer);
            checkAuthExists();
        } else {
            ViewUtils.show(networkErrorContainer);
        }
    }

    @OnClick(R.id.retry)
    public void retryButtonClicked() {
        checkForInternet();
    }

    private void setupLoginFragment() {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.turn_up, R.anim.turn_up)
                .add(R.id.container, new LoginFragment())
                .commit();

        loginPrompt.startAnimation(AnimationUtils.loadAnimation(this, R.anim.abc_slide_in_top));
        ViewUtils.show(loginPrompt);
    }

    private void checkAuthExists() {
        new Thread() {
            public void run() {
                Hawk.init(getApplicationContext(), getString(R.string.prefs_password));
                authExists = Hawk.contains(ProApp.AUTH_KEY);
                handler.post(authCheckComplete);
            }
        }.start();
    }

    private void onAuthCheckComplete() {
        splashContainer.startAnimation(AnimationUtils.loadAnimation(this, R.anim.throw_up));
        ViewUtils.hide(splashContainer);

        if (authExists) {
            GitHubApi.getService(Hawk.get(ProApp.AUTH_KEY));
            startActivity(new Intent(this, ProfileActivity.class));
            finish();
        } else {
            setupLoginFragment();
        }
    }
}
