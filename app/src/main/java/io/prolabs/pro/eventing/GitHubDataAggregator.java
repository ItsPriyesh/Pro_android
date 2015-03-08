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
    private static GitHubDataAggregator instance;
    private GitHubService service;
    private GitHubReceiver receiver;
    private Bus RECEIVE;
    private FullUserStats currentStats;
    private Map<Repo, List<Language>> languagesByRepo;

    public GitHubDataAggregator(GitHubReceiver receiver) {
        service = GitHubApi.getService();

        RECEIVE = new Bus();
        this.receiver = receiver;
        receiver.register(this);
        currentStats = new FullUserStats();
        languagesByRepo = new HashMap<>();
    }

    public void register(Object listener) {
        RECEIVE.register(listener);
    }

    @Subscribe
    public synchronized void receiveLanguages(LanguagesReceived received) {
        languagesByRepo.put(received.getRepo(), received.getLanguages());
        currentStats = currentStats.setLanguagesByRepo(languagesByRepo);
        RECEIVE.post(currentStats);
    }

    @Subscribe
    public synchronized void receiveCodeWeeks(CodeWeeksReceived received) {
        currentStats = currentStats.addWeeksOfCode(received.getRepo(), received.getCodeWeeks());
        RECEIVE.post(currentStats);
    }

    @Subscribe
    public synchronized void receiveCommitActiivity(CommitsReceived received) {
        currentStats = currentStats.addCommits(received.getRepo(), received.getCommitActivity());
        RECEIVE.post(currentStats);
    }

    @Subscribe
    public synchronized void receiveRepos(ReposReceived received) {
        currentStats = currentStats.setRepos(received.getRepos());
        RECEIVE.post(currentStats);
    }

}
