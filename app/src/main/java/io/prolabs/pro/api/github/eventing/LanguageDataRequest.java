package io.prolabs.pro.api.github.eventing;

import java.util.List;

import io.prolabs.pro.models.github.Repo;
import io.prolabs.pro.models.github.User;

/**
 * Created by Edmund on 2015-03-07.
 */
public class LanguageDataRequest {
    private User user;
    private Repo repo;

    public LanguageDataRequest(User user, Repo repo) {
        this.user = user;
        this.repo = repo;
    }

    public User getUser() {
        return user;
    }

    public Repo getRepo() {
        return repo;
    }
}
