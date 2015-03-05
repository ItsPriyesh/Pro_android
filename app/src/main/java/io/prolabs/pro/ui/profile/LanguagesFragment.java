package io.prolabs.pro.ui.profile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

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
import timber.log.Timber;

public class LanguagesFragment extends Fragment {

    @InjectView(R.id.languageList)
    ListView languageListView;

    private GitHubService gitHubService;
    private User user;
    private List<Repo> repos;
    private HashMap<Repo, Integer> retries = new HashMap<>();
    private final TreeMap<String, Integer> languages = new TreeMap<>();
    private final Object languageLock = new Object();
    private final AtomicInteger queriedRepos = new AtomicInteger(0);
    private final AtomicInteger failedRepos = new AtomicInteger(0);
    private static final int MAX_RETRIES = 5;

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
        for (Repo repo : repos) {
            retries.put(repo, 0);
        }
    }

    private void addLanguages(List<Language> langs) {
        for (Language lang : langs) {
            addLanguage(lang);
        }
    }

    private void addLanguage(Language lang) {
        String name = lang.getName();
        int bytes = lang.getBytes();
        synchronized (languageLock) {
            Timber.i("Adding language: " + lang.getName());
            int bytesToAdd = bytes;
            Integer bytesAlreadyPresent = languages.get(name);
            if (bytesAlreadyPresent != null) {
                bytesToAdd += bytesAlreadyPresent;
            }
            languages.put(name, bytesToAdd);
        }
    }

    private void askForLanguages() {
        if (repos.isEmpty()) return;

        for (Repo repo : repos) {
            Timber.i("Repo found: " + repo.getName());
            askForLanguage(repo);
        }
    }

    private void askForLanguage(final Repo repo) {
        gitHubService.getLanguages(user.getUsername(), repo.getName(), new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                addLanguages(GitHubUtils.parseLanguageResponse(jsonElement));
                gotARepo();
                for (Map.Entry language : languages.entrySet())
                    Timber.i("Language received: " + language.getKey() + " : " + language.getValue());
            }

            @Override
            public void failure(RetrofitError error) {
                recoverFromRepoError(error, repo);
            }
        });
    }

    private boolean is404(RetrofitError error) {
        return error.getKind() == RetrofitError.Kind.HTTP &&
                error.getResponse().getStatus() == 404;
    }

    private void recoverFromRepoError(RetrofitError error, Repo repo) {
        RetrofitError.Kind errorKind = error.getKind();
        if (!is404(error)) {
            if (isRetryable(errorKind)) {
                tryToRetry(repo);
            } else {
                Timber.i("Failed to get repo: " + repo.getName());
                Toast.makeText(getActivity(), getErrorMessage(error), Toast.LENGTH_SHORT).show();
                failRepo();
            }
        } else {
            failRepo();
        }
    }

    private void tryToRetry(Repo repo) {
        int retriesSoFar = retries.get(repo);
        if (retriesSoFar == MAX_RETRIES) {
            Toast.makeText(getActivity(), "Repo " + repo.getName() + " not reachable!", Toast.LENGTH_SHORT)
                    .show();
            failRepo();
        } else {
            retries.put(repo, retriesSoFar + 1);
            askForLanguage(repo);
        }
        retries.put(repo, retriesSoFar + 1);
    }

    private void failRepo() {
        if (queriedRepos.get() + failedRepos.incrementAndGet() >= repos.size()) {
            setupUI();
        }
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

    private boolean isRetryable(RetrofitError.Kind errorKind) {
        boolean retryable = false;
        switch (errorKind) {
            case NETWORK:
            case CONVERSION:
                retryable = true;
            case HTTP:
            case UNEXPECTED:
            default:
                retryable = false;
        }
        return retryable;
    }

    private void setupUI() {
        Timber.i("SETUP UI CALLED");
        ArrayList<Language> languageArrayList = new ArrayList<>();
        for (Map.Entry<String, Integer> language : languages.entrySet()) {
            languageArrayList.add(new Language(language.getKey(), language.getValue()));
        }
        Timber.i("Setting up language list...");

        languageListView.setAdapter(
                new LanguageListAdapter(getActivity(), languageArrayList));
    }

    private void gotARepo() {
        if (queriedRepos.incrementAndGet() + failedRepos.get() >= repos.size()) {
            setupUI();
        }
    }
}
