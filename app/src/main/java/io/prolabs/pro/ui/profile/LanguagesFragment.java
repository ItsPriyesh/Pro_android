package io.prolabs.pro.ui.profile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.prolabs.pro.R;
import io.prolabs.pro.api.GitHubService;
import io.prolabs.pro.models.github.Language;
import io.prolabs.pro.models.github.Repo;
import io.prolabs.pro.models.github.User;
import io.prolabs.pro.utils.GitHubUtils;

public class LanguagesFragment extends Fragment {

    @InjectView(R.id.languageList)
    ListView languageListView;

    private GitHubService gitHubService;
    private User user;
    private List<Repo> repos;
    private List<Language> languages;

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

        GitHubUtils.getLanguages(user.getUsername(), repos);

       /* gitHubService = GitHubApi.getService();

        gitHubService.getLanguages(user.getUsername(), "Roomies", new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                languages = GitHubUtils.parseLanguageResponse(jsonElement);
                Collections.sort(languages);
                for (Language language : languages) Timber.i(language.getName() + ":" + language.getBytes());
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });*/

      //  List<String> topLanguages = GitHubUtils.getTopLanguages(repos);
        //languageListView.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, languages));


        return view;
    }

    public void setRepos(List<Repo> repos) {
        this.repos = repos;
    }

}
