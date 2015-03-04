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
import io.prolabs.pro.models.github.Repo;
import io.prolabs.pro.models.github.User;
import io.prolabs.pro.utils.GitHubUtils;

public class InfoFragment extends Fragment {

    @InjectView(R.id.publicReposCount)
    TextView publicReposText;

    @InjectView(R.id.privateReposCount)
    TextView privateReposText;

    @InjectView(R.id.totalStarsCount)
    TextView totalStarsText;

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

        publicReposText.setText("" + user.getPublicRepoCount());
        privateReposText.setText("" + user.getPrivateReposCount());
        totalStarsText.setText("" + GitHubUtils.getTotalStars(repos));

        return view;
    }
}
