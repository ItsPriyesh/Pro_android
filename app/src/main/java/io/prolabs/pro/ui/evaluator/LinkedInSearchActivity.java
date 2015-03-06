package io.prolabs.pro.ui.evaluator;

import android.app.ProgressDialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;

import com.orhanobut.hawk.Hawk;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import io.prolabs.pro.ProApp;
import io.prolabs.pro.R;
import io.prolabs.pro.SecretStuff;
import io.prolabs.pro.ui.common.BaseToolBarActivity;

public class LinkedInSearchActivity extends BaseToolBarActivity
        implements LinkedInLoginFragment.LinkedInDialogListener {

    private Token linkedInRequestToken;
    private Token linkedInAccessToken;
    private OAuthService linkedInService;
    private ProgressDialog progressDialog;
    private boolean linkedinAuthExists = false;

    public static final String AUTH_URL = "AUTH_URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linked_in_search);
        setToolbarTitle("Search LinkedIn");
        showToolbarBackButton();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        checkAuthExists();

    }

    final Handler handler = new Handler();
    final Runnable authCheckComplete = () -> onAuthCheckComplete();
    final Runnable receivedRequestToken = () -> onRequestTokenReceived();

    private void checkAuthExists() {
        new Thread() {
            public void run() {
                Hawk.init(getApplicationContext(), getString(R.string.prefs_password));
                linkedinAuthExists = Hawk.contains(ProApp.LINKEDIN_AUTH_KEY);

                handler.post(authCheckComplete);
            }
        }.start();
    }

    private void onAuthCheckComplete() {
        if (linkedinAuthExists) {

        } else {
            startOAuth();
        }
    }

    private void startOAuth() {
        new Thread() {
            public void run() {
                linkedInService = new ServiceBuilder()
                        .provider(LinkedInApi.class)
                        .apiKey(SecretStuff.LINKEDIN_API_KEY)
                        .apiSecret(SecretStuff.LINKEDIN_API_SECRET)
                        .build();

                linkedInRequestToken = linkedInService.getRequestToken();

                handler.post(receivedRequestToken);
            }
        }.start();
    }

    private void onRequestTokenReceived() {
        showLoginDialog();
    }

    public void showLoginDialog() {
        if (progressDialog.isShowing()) progressDialog.dismiss();

        Bundle args = new Bundle();
        args.putString(AUTH_URL, linkedInService.getAuthorizationUrl(linkedInRequestToken));

        DialogFragment loginDialog = new LinkedInLoginFragment();
        loginDialog.setArguments(args);
        loginDialog.show(getSupportFragmentManager(), "LinkedInLoginFragment");
    }

    @Override
    public void onDoneButtonClicked(String verificationKey) {
        linkedInAccessToken =
                linkedInService.getAccessToken(linkedInRequestToken, new Verifier(verificationKey));
    }
}
