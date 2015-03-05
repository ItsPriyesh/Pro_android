package io.prolabs.pro.ui.profile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;
import io.prolabs.pro.R;
import io.prolabs.pro.api.GitHubApi;
import io.prolabs.pro.api.GitHubService;
import io.prolabs.pro.models.github.Repo;
import io.prolabs.pro.models.github.User;
import io.prolabs.pro.ui.common.SlidingTabLayout;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProfileActivity extends ActionBarActivity {

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
    private User user;
    private List<Repo> repos;
    private InfoFragment infoFragment;
    private LanguagesFragment languagesFragment;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.inject(this);

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
        gitHubService.getAuthUser(new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                setUser(user);
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
                setRepos(repos);
                setupFragments();
                setupInterface();
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
                handleApiCallError();
            }
        });
    }

    private void setupFragments() {
        infoFragment.setRepos(repos);
        infoFragment.setUser(user);
        languagesFragment.setUser(user);
        languagesFragment.setRepos(repos);
    }

    private void setupInterface() {
        if (progressDialog.isShowing()) progressDialog.dismiss();

        viewPager.setAdapter(new ProfilePagerAdapter(getSupportFragmentManager()));
        tabLayout.setViewPager(viewPager);

        Picasso.with(this).load(user.getAvatarUrl()).into(avatarImage);
        usernameText.setText(user.getUsername());
        nameText.setText(user.getName());
    }

    private void handleApiCallError() {
        new AlertDialog.Builder(this)
                .setTitle("Network error")
                .setMessage("An error occurred while fetching your data. Please try again.")
                .setPositiveButton("Retry", (dialog, id) -> startApiCalls())
                .create().show();
    }

    private void setUser(User user) {
        this.user = user;
    }

    private void setRepos(List<Repo> repos) {
        this.repos = repos;
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
