package io.prolabs.pro.ui.profile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.prolabs.pro.R;
import io.prolabs.pro.api.github.GitHubApi;
import io.prolabs.pro.api.github.GitHubService;
import io.prolabs.pro.api.github.eventing.GitHubReceiver;
import io.prolabs.pro.api.github.eventing.LanguageDataRequest;
import io.prolabs.pro.api.github.eventing.LanguagesReceived;
import io.prolabs.pro.models.github.Language;
import io.prolabs.pro.models.github.Repo;
import io.prolabs.pro.models.github.User;
import retrofit.RetrofitError;
import timber.log.Timber;

public class LanguagesFragment extends Fragment {

    @InjectView(R.id.languageList)
    ListView languageListView;

    private GitHubService gitHubService;
    private GitHubReceiver gitHubReceiver;
    private User user;
    private List<Repo> repos;
    private final TreeMap<String, Integer> displayedLanguages = new TreeMap<>();
    private final Object languageLock = new Object();

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
        gitHubReceiver = GitHubReceiver.getInstance();
        gitHubReceiver.register(this);

        askForLanguages();

        return view;
    }

    public void setRepos(List<Repo> repos) {
        this.repos = repos;
    }

    private void askForLanguages() {
        for (Repo repo : this.repos) {
            LanguageDataRequest request = new LanguageDataRequest(user, repo);
            gitHubReceiver.ask(request);
        }
    }

    @Subscribe
    public void receivedALanguage(LanguagesReceived received) {
        updateUI(received.getUser(), received.getLanguages());
    }

    private void updateUI(User user, List<Language> langs) {
        for (Language lang : langs) {
            String name = lang.getName();
            int bytes = lang.getBytes();
            synchronized (languageLock) {
                Timber.i("Adding language: " + lang.getName());
                int bytesToAdd = bytes;
                Integer bytesAlreadyPresent = displayedLanguages.get(name);
                if (bytesAlreadyPresent != null) {
                    bytesToAdd += bytesAlreadyPresent;
                }
                displayedLanguages.put(name, bytesToAdd);
            }
        }
        ArrayList<Language> languageArrayList = new ArrayList<>();
        for (Map.Entry<String, Integer> language : displayedLanguages.entrySet()) {
            languageArrayList.add(new Language(language.getKey(), language.getValue()));
        }
        languageListView.setAdapter(
                new LanguageListAdapter(getActivity(), languageArrayList));
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
