package io.prolabs.pro.ui.profile;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.prolabs.pro.R;
import io.prolabs.pro.algo.FullUserStats;
import io.prolabs.pro.algo.UserXp;
import io.prolabs.pro.algo.XpCalculator;
import io.prolabs.pro.algo.XpCalculators;
import io.prolabs.pro.api.github.GitHubService;
import io.prolabs.pro.eventing.GitHubDataAggregator;
import io.prolabs.pro.eventing.GitHubReceiver;
import io.prolabs.pro.models.github.GitHubUser;
import io.prolabs.pro.models.github.Repo;
import io.prolabs.pro.utils.GitHubUtils;

public class InfoFragment extends Fragment{

    private static final long UPDATE_XP_DELAY = 1000;
    @InjectView(R.id.xpCardView)
    CardView xpCard;

    @InjectView(R.id.generalInfoCard)
    CardView generalInfoCard;

    public static final int XP_SIG_FIGS = 3;
    @InjectView(R.id.publicReposCount)
    TextView publicReposText;

    @InjectView(R.id.privateReposCount)
    TextView privateReposText;

    @InjectView(R.id.totalStarsCount)
    TextView totalStarsText;

    @InjectView(R.id.xpValue)
    TextView xpTextView;

    private GitHubUser gitHubUser;
    private List<Repo> repos;
    private GitHubService gitHubService;
    private GitHubReceiver gitHubReceiver;
    private GitHubDataAggregator aggregator;
    private XpCalculator xpCalculator;
    private volatile boolean updating = false;
    private UserXp currentXp;

    public InfoFragment() {
        // Required empty public constructor
    }

    public void setUser(GitHubUser gitHubUser) {
        this.gitHubUser = gitHubUser;
    }

    public void setRepos(List<Repo> repos) {
        this.repos = repos;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        ButterKnife.inject(this, view);

        publicReposText.setText(String.valueOf(gitHubUser.getPublicRepoCount()));
        privateReposText.setText(String.valueOf(gitHubUser.getPrivateReposCount()));
        totalStarsText.setText(String.valueOf(GitHubUtils.getTotalStars(repos)));

        gitHubReceiver = GitHubReceiver.getInstance();
        xpCalculator = XpCalculators.SIMPLE;
        GitHubDataAggregator aggregator = new GitHubDataAggregator(gitHubReceiver);
        aggregator.register(this);

        return view;
    }

    public static long roundToSignificantFigures(double num, int n) {
        if(num == 0) {
            return 0;
        }

        final double d = Math.ceil(Math.log10(num < 0 ? -num: num));
        final int power = n - (int) d;

        final double magnitude = Math.pow(10, power);
        final long shifted = Math.round(num*magnitude);
        return (long)(shifted/magnitude);
    }

    @Subscribe
    public synchronized void updateXp(FullUserStats stats) {
        if (!updating) {
            updating = true;
            new Handler().postDelayed(this::updateUI, UPDATE_XP_DELAY);
        }
        currentXp = xpCalculator.calculateXp(stats);

    }

    private void updateUI() {
        updating = false;
        long rounded = roundToSignificantFigures(currentXp.getTotalXp(), XP_SIG_FIGS);
        xpTextView.setText(String.valueOf(rounded));
    }

}
