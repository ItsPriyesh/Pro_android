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

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;
import io.prolabs.pro.R;
import io.prolabs.pro.api.github.GitHubApi;
import io.prolabs.pro.api.github.GitHubService;
import io.prolabs.pro.eventing.GitHubRequester;
import io.prolabs.pro.models.github.GitHubUser;
import io.prolabs.pro.ui.common.BaseToolBarActivity;
import io.prolabs.pro.ui.common.SlidingTabLayout;
import io.prolabs.pro.ui.settings.SettingsActivity;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static io.prolabs.pro.utils.CallbackUtils.callback;

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

        showToolbarBackButton();
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
        GitHubApi.getService().getAuthUser(callback(
                gitHubUser -> {
                    setUser(gitHubUser);
                    getAuthUserRepos();
                },

                error -> {
                    progressDialog.dismiss();
                    handleApiCallError();
                }
        ));
    }

    private void getAuthUserRepos() {
        GitHubApi.getService().getRepos(GitHubApi.MAX_REPOS_PER_PAGE, callback(
                repos -> {
                    requestData();
                    setupInterface();
                },

                error -> {
                    progressDialog.dismiss();
                    handleApiCallError();
                }
        ));
    }

    private void requestData() {
        GitHubRequester.getInstance().requestAllStats();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                requestData();
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
            return tabTitles.length;
        }
    }
}
