package io.prolabs.pro.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.prolabs.pro.R;
import io.prolabs.pro.api.github.GitHubApi;
import io.prolabs.pro.models.github.GitHubUser;
import io.prolabs.pro.ui.common.BaseToolBarActivity;
import io.prolabs.pro.ui.profile.ProfileActivity;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends BaseToolBarActivity {

    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @InjectView(R.id.profile_pic)
    CircleImageView circleImageView;

    @InjectView(R.id.username)
    TextView username;

    @InjectView(R.id.name)
    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.primary_dark));

        GitHubApi.getService().getAuthUser(new Callback<GitHubUser>() {
            @Override
            public void success(GitHubUser gitHubUser, Response response) {
                Picasso.with(MainActivity.this).load(gitHubUser.getAvatarUrl()).into(circleImageView);
                username.setText(gitHubUser.getUsername());
                name.setText(gitHubUser.getName());
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    @OnClick(R.id.header)
    public void openProfile() {
        startActivity(new Intent(this, ProfileActivity.class));
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
