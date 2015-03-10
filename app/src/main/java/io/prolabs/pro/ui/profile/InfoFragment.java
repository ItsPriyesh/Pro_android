package io.prolabs.pro.ui.profile;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.prolabs.pro.R;
import io.prolabs.pro.Tips;
import io.prolabs.pro.algo.FullUserStats;
import io.prolabs.pro.algo.UserXp;
import io.prolabs.pro.algo.XpCalculator;
import io.prolabs.pro.algo.XpCalculators;
import io.prolabs.pro.api.github.GitHubApi;
import io.prolabs.pro.eventing.GitHubDataAggregator;
import io.prolabs.pro.eventing.GitHubRequester;
import io.prolabs.pro.models.github.GitHubUser;
import io.prolabs.pro.models.github.Repo;
import io.prolabs.pro.ui.common.SwipeDismissTouchListener;
import io.prolabs.pro.utils.GitHubUtils;
import io.prolabs.pro.utils.ViewUtils;

public class InfoFragment extends Fragment {

    private static final long UPDATE_XP_DELAY = 1000;
    @InjectView(R.id.xpCardView)
    CardView xpCard;

    @InjectView(R.id.generalInfoCard)
    CardView generalInfoCard;

    @InjectView(R.id.tipsContainer)
    ViewGroup tipsContainer;

    @InjectView(R.id.publicReposCount)
    TextView publicReposText;

    @InjectView(R.id.privateReposCount)
    TextView privateReposText;

    @InjectView(R.id.totalStarsCount)
    TextView totalStarsText;

    @InjectView(R.id.xpValue)
    TextView xpTextView;

    private List<Repo> repos = new ArrayList<>();
    private XpCalculator xpCalculator;
    private volatile boolean updating = false;
    private volatile FullUserStats currentStats;

    public InfoFragment() {
        // Required empty public constructor
    }

    public static long roundToSignificantFigures(double num) {
        if (num == 0) {
            return 0;
        }

        final double d = Math.ceil(Math.log10(num < 0 ? -num : num));
        final int n = (int) Math.ceil(d / 2);
        final int power = n - (int) d;

        final double magnitude = Math.pow(10, power);
        final long shifted = Math.round(num * magnitude);
        return (long) (shifted / magnitude);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        ButterKnife.inject(this, view);

        GitHubUser user = GitHubApi.getCurrentUser();

        publicReposText.setText(String.valueOf(user.getPublicRepoCount()));
        privateReposText.setText(String.valueOf(user.getPrivateReposCount()));

        xpCalculator = XpCalculators.SIMPLE;
        GitHubDataAggregator aggregator = GitHubDataAggregator.getInstance();
        aggregator.register(this);

        CardView cardView = createTipCard();

        cardView.setOnTouchListener(getListener(cardView));

        tipsContainer.addView(cardView);

        return view;
    }

    private CardView createTipCard() {
        int margin = ViewUtils.dpToPx(16, getActivity());

        LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        containerParams.setMargins(margin, 0, margin, margin);

        CardView.LayoutParams cardParams = new CardView.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(margin, margin, margin, margin);

        CardView cardView = new CardView(getActivity());
        cardView.setLayoutParams(containerParams);
        cardView.setClickable(true);
        cardView.setOnTouchListener(getListener(cardView));

        TextView tipText = new TextView(getActivity());
        tipText.setLayoutParams(cardParams);
        tipText.setText(Tips.TIPS[(int) (Math.floor(Math.random() * Tips.TIPS.length))]);

        cardView.addView(tipText);

        return cardView;
    }

    private final SwipeDismissTouchListener getListener(CardView cardView) {
        return new SwipeDismissTouchListener(
                cardView,
                null,
                new SwipeDismissTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(Object token) {
                        return true;
                    }

                    @Override
                    public void onDismiss(View view, Object token) {
                        tipsContainer.removeView(cardView);
                        tipsContainer.addView(createTipCard());
                    }
                });
    }


    @Subscribe
    public synchronized void updateStats(FullUserStats stats) {
        if (!updating) {
            updating = true;
            new Handler().postDelayed(this::updateUI, UPDATE_XP_DELAY);
        }
        currentStats = stats;

    }

    private void updateUI() {
        List<Repo> repoList = new ArrayList<>(currentStats.getRepos());
        totalStarsText.setText(String.valueOf(GitHubUtils.getTotalStars(repoList)));
        UserXp currentXp = xpCalculator.calculateXp(currentStats);
        long rounded = roundToSignificantFigures(currentXp.getTotalXp());
        xpTextView.setText(String.valueOf(rounded));
        updating = false;
    }

}
