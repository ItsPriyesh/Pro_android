package io.prolabs.pro.ui.profile;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.picasso.Picasso;

import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;
import io.prolabs.pro.R;
import io.prolabs.pro.api.GitHubApi;
import io.prolabs.pro.api.GitHubService;
import io.prolabs.pro.models.github.User;
import io.prolabs.pro.ui.common.BaseToolbarActivity;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProfileActivity extends BaseToolbarActivity {

    @InjectView(R.id.avatar)
    CircleImageView avatarImage;

    private GitHubService gitHubService;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        gitHubService = GitHubApi.getService();
        gitHubService.getAuthUser(new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                setUser(user);
                setupInterface();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void setupInterface() {
        setToolbarTitle(user.getLogin());

        Picasso.with(this).load(user.getAvatarUrl()).into(avatarImage);
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
