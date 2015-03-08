package io.prolabs.pro.algo;

import java.util.List;

import io.prolabs.pro.models.github.CodeWeek;
import io.prolabs.pro.models.github.CommitActivity;
import io.prolabs.pro.models.github.Language;
import io.prolabs.pro.models.github.Repo;

/**
 * Created by Edmund on 2015-03-07.
 */
public class FullUserStats {
    CommitActivity commits;
    List<CodeWeek> weeksOfCode;
    List<Language> languages;
    List<Repo> repos;

    public FullUserStats(CommitActivity commits, List<CodeWeek> weeksOfCode, List<Language> languages, List<Repo> repos) {
        this.commits = commits;
        this.weeksOfCode = weeksOfCode;
        this.languages = languages;
        this.repos = repos;
    }

    public List<Language> getLanguages() {
        return languages;
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
