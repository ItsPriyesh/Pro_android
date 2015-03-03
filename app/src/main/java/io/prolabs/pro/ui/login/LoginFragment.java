package io.prolabs.pro.ui.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.prolabs.pro.R;
import io.prolabs.pro.api.GitHubApi;
import io.prolabs.pro.api.GitHubService;
import io.prolabs.pro.models.github.User;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

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
        Timber.i(username + password);
        gitHubService = GitHubApi.getService(username, password);

        gitHubService.getAuthUser(new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                GitHubApi.saveCurrentAuth();
                Timber.i("Private repos " + user.getPrivateReposCount());
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

}
