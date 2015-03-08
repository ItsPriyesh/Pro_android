package io.prolabs.pro.ui.profile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.common.collect.Ordering;
import com.squareup.otto.Subscribe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.prolabs.pro.R;
import io.prolabs.pro.api.github.GitHubApi;
import io.prolabs.pro.api.github.GitHubService;
import io.prolabs.pro.api.github.eventing.GitHubReceiver;
import io.prolabs.pro.api.github.eventing.LanguagesReceived;
import io.prolabs.pro.api.github.eventing.ResetDataRequest;
import io.prolabs.pro.models.github.Language;
import io.prolabs.pro.models.github.Repo;
import io.prolabs.pro.models.github.GitHubUser;
import io.prolabs.pro.utils.ValueComparableMap;
import retrofit.RetrofitError;
import timber.log.Timber;

public class LanguagesFragment extends Fragment {

    private final Object languageLock = new Object();
    @InjectView(R.id.languageList)
    ListView languageListView;
    private Map<String, Long> displayedLanguages = new HashMap<>();
    private GitHubService gitHubService;
    private GitHubReceiver gitHubReceiver;
    private GitHubUser user;
    private List<Repo> repos;

    public LanguagesFragment() {
        // Required empty public constructor
    }

    public void setUser(GitHubUser user) {
        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_languages, container, false);
        ButterKnife.inject(this, view);

        gitHubService = GitHubApi.getService();
        gitHubReceiver = GitHubReceiver.getInstance();
        gitHubReceiver.register(this);

        gitHubReceiver.requestAllLanguages();

        return view;
    }

    public void setRepos(List<Repo> repos) {
        this.repos = repos;
    }

    @Subscribe
    public void resetData(ResetDataRequest request) {
        synchronized (languageLock) {
            displayedLanguages = new HashMap<>();
        }
    }

    @Subscribe
    public void receivedALanguage(LanguagesReceived received) {
        updateUI(received.getUser(), received.getLanguages());
    }

    private void updateUI(GitHubUser user, List<Language> langs) {
        for (Language lang : langs) {
            String name = lang.getName();
            long bytes = lang.getBytes();
            synchronized (languageLock) {
                Timber.i("Adding language: " + lang.getName());
                long bytesToAdd = bytes;
                if (displayedLanguages.containsKey(name)) {
                    bytesToAdd += displayedLanguages.get(name);
                }
                displayedLanguages.put(name, bytesToAdd);
            }
        }
        languageListView.setAdapter(
                new LanguageAdapter(getActivity(), displayedLanguages));
    }

    private String getErrorMessage(RetrofitError error) {
        RetrofitError.Kind errorKind = error.getKind();
        switch (errorKind) {
            case NETWORK:
                return "Network error!";
            case CONVERSION:
                return "Data read error!";
            case HTTP:
                return "HTTP problem: status " + error.getResponse().getStatus();
            default:
                return "Something went wrong fetching your information!";
        }
    }
}
