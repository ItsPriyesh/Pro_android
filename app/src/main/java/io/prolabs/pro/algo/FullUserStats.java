package io.prolabs.pro.algo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.prolabs.pro.models.github.CodeWeek;
import io.prolabs.pro.models.github.CommitActivity;
import io.prolabs.pro.models.github.Language;
import io.prolabs.pro.models.github.Repo;

/**
 * Created by Edmund on 2015-03-07.
 */
public class FullUserStats {
    private final Map<? extends Repo, ? extends List<CommitActivity>> commits;
    private final Map<Repo, List<CodeWeek>> weeksOfCodeByRepo;
    private final Map<Repo, List<Language>> languagesByRepo;
    private final Set<Repo> repos;

    public FullUserStats(Map<? extends Repo, ? extends List<CommitActivity>> commits, Map<Repo, List<CodeWeek>> weeksOfCode, Map<Repo, List<Language>> languagesByRepo, Set<Repo> repos) {
        this.commits = commits;
        this.weeksOfCodeByRepo = weeksOfCode;
        this.languagesByRepo = languagesByRepo;
        this.repos = repos;
    }

    public FullUserStats() {
        this.commits = new HashMap<>();
        this.repos = new HashSet<>();
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

    public FullUserStats addLanguagesByRepo(Repo repo, List<Language> languages) {
        Map<Repo, List<Language>> newLanguagesByRepo = new HashMap<>();
        newLanguagesByRepo.putAll(languagesByRepo);
        newLanguagesByRepo.put(repo, languages);
        return setLanguagesByRepo(newLanguagesByRepo);
    }

    public Set<Repo> getRepos() {
        return repos;
    }

    public FullUserStats setRepos(Set<Repo> repos) {
        return new FullUserStats(commits, weeksOfCodeByRepo, languagesByRepo, repos);
    }

    public Map<? extends Repo, ? extends List<CommitActivity>> getCommits() {
        return commits;
    }

    public FullUserStats addCommits(Repo repo, List<CommitActivity> activity) {
        Map<Repo, List<CommitActivity>> newWeeks = new HashMap<>();
        newWeeks.putAll(commits);
        newWeeks.put(repo, activity);
        return setCommits(newWeeks);
    }

    public FullUserStats setCommits(Map<Repo, List<CommitActivity>> commits) {
        return new FullUserStats(commits, weeksOfCodeByRepo, languagesByRepo, repos);
    }

    public Map<Repo, List<CodeWeek>> getWeeksOfCodeByRepo() {
        return weeksOfCodeByRepo;
    }
}
