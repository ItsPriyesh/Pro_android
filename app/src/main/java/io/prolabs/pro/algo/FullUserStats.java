package io.prolabs.pro.algo;

import com.google.common.base.Optional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
    private final Optional<CommitActivity> commits;
    private final Map<Repo, List<CodeWeek>> weeksOfCodeByRepo;
    private final Map<Repo, List<Language>> languagesByRepo;
    private final List<Repo> repos;

    public FullUserStats(Map<Repo, List<CodeWeek>> weeksOfCode, Map<Repo, List<Language>> languagesByRepo, List<Repo> repos) {
        this.commits = Optional.absent();
        this.repos = repos;
        this.weeksOfCodeByRepo = weeksOfCode;
        this.languagesByRepo = languagesByRepo;
    }

    public FullUserStats(Optional<CommitActivity> commits, Map<Repo, List<CodeWeek>> weeksOfCode, Map<Repo, List<Language>> languagesByRepo, List<Repo> repos) {
        this.commits = commits;
        this.weeksOfCodeByRepo = weeksOfCode;
        this.languagesByRepo = languagesByRepo;
        this.repos = repos;
    }

    public FullUserStats() {
        this.commits = Optional.absent();
        this.repos = new ArrayList<>();
        this.weeksOfCodeByRepo = new HashMap<>();
        this.languagesByRepo = new HashMap<>();
    }

    public FullUserStats setWeeksOfCode(Map<Repo, List<CodeWeek>> weeksOfCode) {
        return new FullUserStats(commits, weeksOfCode, languagesByRepo, repos);
    }

    public FullUserStats addWeeksOfCode(Repo repo, List<CodeWeek> codeWeeks) {
        Map<Repo, List<CodeWeek>> newWeeks = new HashMap<>();
        newWeeks.putAll(weeksOfCodeByRepo);
        newWeeks.put(repo, codeWeeks);
        return setWeeksOfCode(newWeeks);
    }

    public Map<Repo, List<Language>> getLanguagesByRepo() {
        return languagesByRepo;
    }

    public FullUserStats setLanguagesByRepo(Map<Repo, List<Language>> languagesByRepo) {
        return new FullUserStats(commits, weeksOfCodeByRepo, languagesByRepo, repos);
    }

    public List<Repo> getRepos() {
        return repos;
    }

    public FullUserStats setRepos(List<Repo> repos) {
        return new FullUserStats(commits, weeksOfCodeByRepo, languagesByRepo, repos);
    }

    public Optional<CommitActivity> getCommits() {
        return commits;
    }

    public FullUserStats setCommits(Optional<CommitActivity> commits) {
        return new FullUserStats(commits, weeksOfCodeByRepo, languagesByRepo, repos);
    }

    public Map<Repo, List<CodeWeek>> getWeeksOfCodeByRepo() {
        return weeksOfCodeByRepo;
    }
}
