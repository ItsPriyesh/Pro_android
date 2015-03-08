package io.prolabs.pro.eventing;

import io.prolabs.pro.models.github.Repo;
import io.prolabs.pro.models.github.GitHubUser;

/**
 * Created by Edmund on 2015-03-07.
 */
public class LanguageDataRequest {
    private GitHubUser user;
    private Repo repo;

    public LanguageDataRequest(GitHubUser user, Repo repo) {
        this.user = user;
        this.repo = repo;
    }

    public GitHubUser getUser() {
        return user;
    }

    public Repo getRepo() {
        return repo;
    }
}
