package io.prolabs.pro.ui.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;
import io.prolabs.pro.R;
import io.prolabs.pro.api.GitHubApi;
import io.prolabs.pro.api.GitHubService;
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
    private InfoFragment infoFragment;
    private LanguagesFragment languagesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.inject(this);

        infoFragment = new InfoFragment();
        languagesFragment = new LanguagesFragment();

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

        tabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        tabLayout.setSelectedIndicatorColors(getResources().getColor(android.R.color.white));
        tabLayout.setDistributeEvenly(true);
    }

    private void setupInterface() {
        infoFragment.setUser(user);
        languagesFragment.setUser(user);

        viewPager.setAdapter(new ProfilePagerAdapter(getSupportFragmentManager()));
        tabLayout.setViewPager(viewPager);

        Picasso.with(this).load(user.getAvatarUrl()).into(avatarImage);
        usernameText.setText(user.getUsername());
        nameText.setText(user.getName());
    }

    public void setUser(User user) {
        this.user = user;
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
