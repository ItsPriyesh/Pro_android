package io.prolabs.pro.ui.profile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.prolabs.pro.R;
import io.prolabs.pro.api.GitHubApi;
import io.prolabs.pro.api.GitHubService;
import io.prolabs.pro.models.github.Language;
import io.prolabs.pro.models.github.Repo;
import io.prolabs.pro.models.github.User;
import io.prolabs.pro.utils.GitHubUtils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LanguagesFragment extends Fragment {

    @InjectView(R.id.languageList)
    ListView languageListView;

    private GitHubService gitHubService;
    private User user;
    private List<Repo> repos;
    private final Set<Language> languages = Collections.synchronizedSet(new TreeSet<>());
    private final AtomicLong queriedRepos = new AtomicLong(0);

    public LanguagesFragment() {
        // Required empty public constructor
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_languages, container, false);
        ButterKnife.inject(this, view);

        gitHubService = GitHubApi.getService();

        askForLanguages();

        return view;
    }

    public void setRepos(List<Repo> repos) {
        this.repos = repos;
    }

    private void askForLanguages() {
        if (repos.isEmpty()) return;
        for (Repo r : repos) {
            gitHubService.getLanguages(user.getUsername(), r.getName(), new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    languages.addAll(GitHubUtils.parseLanguageResponse(jsonElement));
                    gotARepo();
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }
    }

    private void setupUI() {
        languageListView.setAdapter(
                new LanguageListAdapter(getActivity(), new ArrayList<>(languages)));
    }

    private void gotARepo() {
        if (queriedRepos.incrementAndGet() == repos.size()) {
            setupUI();
        }
    }
}
