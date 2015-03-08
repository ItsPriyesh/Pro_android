package io.prolabs.pro.eventing;

import java.util.List;

import io.prolabs.pro.models.github.Language;
import io.prolabs.pro.models.github.Repo;

/**
 * Created by Edmund on 2015-03-07.
 */
public class LanguagesReceived {
    private Repo repo;
    private List<Language> languages;

    public LanguagesReceived(Repo repo, List<Language> languages) {
        this.repo = repo;
        this.languages = languages;
    }

    public Repo getRepo() {
        return repo;
    }


    public List<Language> getLanguages() {
        return languages;
    }
}
