package io.prolabs.pro.eventing;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.prolabs.pro.algo.FullUserStats;
import io.prolabs.pro.api.github.GitHubApi;
import io.prolabs.pro.api.github.GitHubService;
import io.prolabs.pro.models.github.Language;
import io.prolabs.pro.models.github.Repo;

/**
 * Created by Edmund on 2015-03-08.
 */
public class GitHubDataAggregator {
    private static GitHubDataAggregator instance = new GitHubDataAggregator();
    private Bus bus;
    private FullUserStats currentStats;

    private GitHubDataAggregator() {
        GitHubRequester.getInstance().register(this);
        bus = new Bus();
        currentStats = new FullUserStats();
    }

    public static GitHubDataAggregator getInstance() {
        return instance;
    }

    public void register(Object listener) {
        bus.register(listener);
    }

    @Subscribe
    public synchronized void receiveLanguages(LanguagesReceived received) {
        currentStats = currentStats.addLanguagesByRepo(received.getRepo(), received.getLanguages());
        bus.post(currentStats);
    }

    @Subscribe
    public synchronized void receiveCodeWeeks(CodeWeeksReceived received) {
        currentStats = currentStats.addWeeksOfCode(received.getRepo(), received.getCodeWeeks());
        bus.post(currentStats);
    }

    @Subscribe
    public synchronized void receiveCommitActiivity(CommitsReceived received) {
        currentStats = currentStats.addCommits(received.getRepo(), received.getCommitActivity());
        bus.post(currentStats);
    }

    @Subscribe
    public synchronized void receiveRepos(ReposReceived received) {
        currentStats = currentStats.setRepos(received.getRepos());
        bus.post(currentStats);
    }

}
