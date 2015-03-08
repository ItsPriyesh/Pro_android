package io.prolabs.pro.eventing;

import java.util.Set;

import io.prolabs.pro.models.github.Repo;

/**
 * Created by Edmund on 2015-03-08.
 */
public class ReposReceived {
    private Set<Repo> repos;

    public Set<Repo> getRepos() {
        return repos;
    }

    public ReposReceived(Set<Repo> repos) {
        this.repos = repos;
    }
}
