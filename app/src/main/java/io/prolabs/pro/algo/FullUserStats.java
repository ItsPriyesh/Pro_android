package io.prolabs.pro.algo;

import java.util.List;
import java.util.Map;

import io.prolabs.pro.models.github.CodeWeek;
import io.prolabs.pro.models.github.CommitActivity;
import io.prolabs.pro.models.github.Language;
import io.prolabs.pro.models.github.Repo;

/**
 * Created by Edmund on 2015-03-07.
 */
public class FullUserStats {
    private final CommitActivity commits;
    private final List<CodeWeek> weeksOfCode;
    private final Map<Repo, List<Language>> languagesByRepo;
    private final List<Repo> repos;

    public FullUserStats(CommitActivity commits, List<CodeWeek> weeksOfCode, Map<Repo, List<Language>> languagesByRepo, List<Repo> repos) {

        this.commits = commits;
        this.weeksOfCode = weeksOfCode;
        this.languagesByRepo = languagesByRepo;
        this.repos = repos;
    }

    public Map<Repo, List<Language>> getLanguagesByRepo() {
        return languagesByRepo;
    }

    public List<Repo> getRepos() {
        return repos;
    }

    public CommitActivity getCommits() {
        return commits;
    }

    public List<CodeWeek> getWeeksOfCode() {
        return weeksOfCode;
    }
}
