package io.prolabs.pro.ui.profile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.prolabs.pro.R;
import io.prolabs.pro.api.github.GitHubApi;
import io.prolabs.pro.api.github.GitHubService;
import io.prolabs.pro.models.github.CommitActivity;
import io.prolabs.pro.models.github.Repo;
import io.prolabs.pro.models.github.User;
import io.prolabs.pro.utils.GitHubUtils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

public class InfoFragment extends Fragment {

    @InjectView(R.id.publicReposCount)
    TextView publicReposText;

    @InjectView(R.id.privateReposCount)
    TextView privateReposText;

    @InjectView(R.id.totalStarsCount)
    TextView totalStarsText;

    @InjectView(R.id.annualContributions)
    TextView annualContributionsText;

    private GitHubService gitHubService;
    private User user;
    private List<Repo> repos;

    public InfoFragment() {
        // Required empty public constructor
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setRepos(List<Repo> repos) {
        this.repos = repos;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        ButterKnife.inject(this, view);

        gitHubService = GitHubApi.getService();
        for (Repo r : repos) {
            Timber.i("STARTING");
            gitHubService.getCommitActivity(user.getUsername(), r.getName(), new Callback<CommitActivity>() {
                @Override
                public void success(CommitActivity commitActivity, Response response) {
                    Timber.i("GOT COMMIT");
                    annualContributionsText.setText(
                            String.valueOf(Integer.parseInt(annualContributionsText.getText().toString()) + commitActivity.getTotalCommits()));
                }

                @Override
                public void failure(RetrofitError error) {
                    Timber.i("FAILED UGH");
                }
            });
        }

        publicReposText.setText(String.valueOf(user.getPublicRepoCount()));
        privateReposText.setText(String.valueOf(user.getPrivateReposCount()));
        totalStarsText.setText(String.valueOf(GitHubUtils.getTotalStars(repos)));

        return view;
    }
}
