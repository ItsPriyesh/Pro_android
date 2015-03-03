package io.prolabs.pro.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.prolabs.pro.R;
import io.prolabs.pro.api.GitHubApi;
import io.prolabs.pro.api.GitHubService;
import io.prolabs.pro.models.github.User;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;


public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.username)
    EditText usernameInput;

    @InjectView(R.id.password)
    EditText passwordInput;
    @InjectView(R.id.button)
    Button button;

    private String username;
    private String password;

    private GitHubService gitHubService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        button.setOnClickListener(v -> {
            username = usernameInput.getText().toString();
            password = passwordInput.getText().toString();

            gitHubService = GitHubApi.getService(username, password);

            gitHubService.getAuthUser(new Callback<User>() {
                @Override
                public void success(User user, Response response) {
                    Timber.i("Private repos " + user.getPrivateReposCount());
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });

        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
