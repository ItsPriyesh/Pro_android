package io.prolabs.pro.ui.login;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.prolabs.pro.R;
import io.prolabs.pro.api.github.GitHubApi;
import io.prolabs.pro.api.github.GitHubService;
import io.prolabs.pro.models.github.GitHubUser;
import io.prolabs.pro.ui.profile.ProfileActivity;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginFragment extends Fragment {

    @InjectView(R.id.usernameInput)
    EditText usernameInput;

    @InjectView(R.id.passwordInput)
    EditText passwordInput;

    private String username;
    private String password;

    private GitHubService gitHubService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick(R.id.loginButton)
    public void loginButtonClicked() {
        username = usernameInput.getText().toString();
        password = passwordInput.getText().toString();

        if (username.equals("") || password.equals("")) {
            Toast.makeText(getActivity(),
                    "Please enter a username and password.", Toast.LENGTH_SHORT).show();
        } else {
            loginGitHub(username, password);
        }
    }

    private void loginGitHub(String username, String password) {
        gitHubService = GitHubApi.getService(username, password);
        gitHubService.getAuthUser(new Callback<GitHubUser>() {
            @Override
            public void success(GitHubUser user, Response response) {
                GitHubApi.saveCurrentAuth();
                startActivity(new Intent(getActivity(), ProfileActivity.class));
                getActivity().finish();
            }

            @Override
            public void failure(RetrofitError error) {
                handleLoginError();
            }
        });
    }

    private void handleLoginError() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Login failed")
                .setMessage("We were unable to login to your GitHub account. " +
                        "Make sure that the credentials you entered are correct.")
                .setPositiveButton("OK", (dialog, id) -> dialog.dismiss())
                .create().show();
    }
}
