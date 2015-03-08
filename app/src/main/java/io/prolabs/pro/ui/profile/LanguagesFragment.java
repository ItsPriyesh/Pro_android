package io.prolabs.pro.ui.profile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.squareup.otto.Subscribe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.prolabs.pro.R;
import io.prolabs.pro.api.github.GitHubApi;
import io.prolabs.pro.api.github.GitHubService;
import io.prolabs.pro.eventing.GitHubReceiver;
import io.prolabs.pro.eventing.LanguagesReceived;
import io.prolabs.pro.models.github.Language;
import io.prolabs.pro.models.github.Repo;
import io.prolabs.pro.models.github.GitHubUser;
import retrofit.RetrofitError;
import timber.log.Timber;

public class LanguagesFragment extends Fragment {

    private final Object languageLock = new Object();
    @InjectView(R.id.languageList)
    ListView languageListView;
    private GitHubService gitHubService;
    private GitHubReceiver gitHubReceiver;
    private HashMap<Repo, List<Language>> languagesByRepo = new HashMap<>();

    public LanguagesFragment() {
        // Required empty public constructor
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

    @Subscribe
    public void receivedALanguage(LanguagesReceived received) {
        languagesByRepo.put(received.getRepo(), received.getLanguages());
        synchronized (languageLock) {
            updateUI();
        }
    }

    private void updateUI() {
        Map<String, Long> displayedLanguages = new HashMap<>();
        for (List<Language> langs : languagesByRepo.values()) {
            for (Language lang : langs) {
                String name = lang.getName();
                long bytes = lang.getBytes();
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
}
