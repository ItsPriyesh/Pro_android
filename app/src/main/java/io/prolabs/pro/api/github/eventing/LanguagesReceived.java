package io.prolabs.pro.api.github.eventing;

import java.util.List;

import io.prolabs.pro.models.github.Language;
import io.prolabs.pro.models.github.User;

/**
 * Created by Edmund on 2015-03-07.
 */
public class LanguagesReceived {
    private List<Language> languages;
    private User user;

    public LanguagesReceived(User user, List<Language> languages) {
        this.languages = languages;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public List<Language> getLanguages() {
        return languages;
    }
}
