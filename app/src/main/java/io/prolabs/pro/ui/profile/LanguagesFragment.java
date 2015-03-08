package io.prolabs.pro.ui.profile;


import android.os.Bundle;
import android.os.Handler;
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

    private static final long UPDATE_LANG_DELAY = 1000;
    @InjectView(R.id.languageList)
    ListView languageListView;
    private GitHubService gitHubService;
    private GitHubReceiver gitHubReceiver;
    private volatile boolean updating = false;
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

        gitHubReceiver.requestAllStats();

        return view;
    }

    @Subscribe
    public synchronized void receivedALanguage(LanguagesReceived received) {
        languagesByRepo.put(received.getRepo(), received.getLanguages());
        if (!updating) {
            updating = true;
            new Handler().postDelayed(this::updateUI, UPDATE_LANG_DELAY);
        }
    }

    private synchronized void updateUI() {
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
        updating = false;
    }
}
