package io.prolabs.pro.ui.profile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.prolabs.pro.R;
import io.prolabs.pro.api.github.GitHubApi;
import io.prolabs.pro.api.github.GitHubService;
import io.prolabs.pro.api.github.eventing.GitHubReceiver;
import io.prolabs.pro.models.github.Repo;
import io.prolabs.pro.models.github.GitHubUser;
import io.prolabs.pro.ui.common.BaseToolBarActivity;
import io.prolabs.pro.ui.common.SlidingTabLayout;
import io.prolabs.pro.ui.evaluator.LinkedInSearchActivity;
import io.prolabs.pro.ui.evaluator.ManualActivity;
import io.prolabs.pro.ui.settings.SettingsActivity;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProfileActivity extends BaseToolBarActivity {

    @InjectView(R.id.avatar)
    CircleImageView avatarImage;

    @InjectView(R.id.username)
    TextView usernameText;

    @InjectView(R.id.name)
    TextView nameText;

    @InjectView(R.id.tabLayout)
    SlidingTabLayout tabLayout;

    @InjectView(R.id.viewPager)
    ViewPager viewPager;

    private GitHubService gitHubService;
    private GitHubUser gitHubUser;
    private InfoFragment infoFragment;
    private LanguagesFragment languagesFragment;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.inject(this);

        disableToolbarElevation();

        infoFragment = new InfoFragment();
        languagesFragment = new LanguagesFragment();

        gitHubService = GitHubApi.getService();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();
        startApiCalls();

        tabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        tabLayout.setSelectedIndicatorColors(getResources().getColor(android.R.color.white));
        tabLayout.setDistributeEvenly(true);
    }

    private void startApiCalls() {
        getAuthUser();
    }

    private void getAuthUser() {
        gitHubService.getAuthUser(new Callback<GitHubUser>() {
            @Override
            public void success(GitHubUser gitHubUser, Response response) {
                setUser(gitHubUser);
                getAuthUserRepos();
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
                handleApiCallError();
            }
        });
    }

    private void getAuthUserRepos() {
        gitHubService.getRepos(GitHubApi.MAX_REPOS_PER_PAGE, new Callback<List<Repo>>() {
            @Override
            public void success(List<Repo> repos, Response response) {
                setupFragments(repos);
                setupInterface();
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
                handleApiCallError();
            }
        });
    }

    private void setupFragments(List<Repo> repos) {
        infoFragment.setRepos(repos);
        infoFragment.setUser(gitHubUser);
        languagesFragment.setUser(gitHubUser);
        languagesFragment.setRepos(repos);
    }

    private void setupInterface() {
        if (progressDialog.isShowing()) progressDialog.dismiss();

        viewPager.setAdapter(new ProfilePagerAdapter(getSupportFragmentManager()));
        tabLayout.setViewPager(viewPager);

        Picasso.with(this).load(gitHubUser.getAvatarUrl()).into(avatarImage);
        usernameText.setText(gitHubUser.getUsername());
        nameText.setText(gitHubUser.getName());
    }

    private void handleApiCallError() {
        new AlertDialog.Builder(this)
                .setTitle("Network error")
                .setMessage("An error occurred while fetching your data. Please try again.")
                .setPositiveButton("Retry", (dialog, id) -> startApiCalls())
                .create().show();
    }

    private void setUser(GitHubUser gitHubUser) {
        this.gitHubUser = gitHubUser;
    }

    @OnClick(R.id.search_fab)
    public void searchFabClicked() {
        showJobSelector();
    }

    private void showJobSelector() {
        final String[] options = {"Enter manually", "Search LinkedIn"};
        new AlertDialog.Builder(this)
                .setTitle("Job description")
                .setItems(options, (dialog, index) -> {
                    switch (index) {
                        case 0:
                            startActivity(new Intent(this, ManualActivity.class));
                            break;
                        case 1:
                            startActivity(new Intent(this, LinkedInSearchActivity.class));
                            break;
                    }
                })
                .create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                GitHubReceiver.getInstance().requestAllLanguages();
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ProfilePagerAdapter extends FragmentPagerAdapter {
        public String[] tabTitles = {"Profile", "Languages"};

        public ProfilePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return infoFragment;
                case 1:
                    return languagesFragment;
                default:
                    return infoFragment;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
