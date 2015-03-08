package io.prolabs.pro.algo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.prolabs.pro.models.github.CodeWeek;
import io.prolabs.pro.models.github.CommitActivity;
import io.prolabs.pro.models.github.Language;
import io.prolabs.pro.models.github.Repo;

/**
 * Created by Edmund on 2015-03-08.
 */
public class SimpleXpCalculator implements XpCalculator {

    private static final long ONE_WEEK = 1000 * 60 * 60 * 24 * 7;
    private static final double MAX_DAYS = 730.0;
    private static final double SCALE = 0.5;

    @Override
    public UserXp calculateXp(FullUserStats stats) {
        Map<? extends Repo, ? extends List<CommitActivity>> commitActivity = stats.getCommits();
        Map<Repo, List<Language>> languagesByRepo = stats.getLanguagesByRepo();
        Map<Repo, List<CodeWeek>> codeWeeks = stats.getWeeksOfCodeByRepo();
        Set<Repo> repos = stats.getRepos();
        double totalXp = 0;
        Date now = new Date();

//        for (List<CodeWeek> weeks : codeWeeks.values()) {
//            for (CodeWeek week : weeks) {
//                Date date = week.getWeekStart();
//                long added = week.getAddedLines();
//                long deleted = week.getDeletedLines();
//                long linesChanged = added - deleted;
//                long dayDiff = (now.getTime() - date.getTime()) / (1000L * 60L * 60L * 24L);
//                totalXp += (long) ((linesChanged) * Math.exp(MAX_DAYS - dayDiff) * 0.001);
//            }
//        }
//
        long popularity = 0;
        for (Repo repo : repos) {
            if (repo.isPrivate()) continue;
            long forks = repo.getForks();
            long stars = repo.getStars();
            long watchers = repo.getWatchers() - stars;
            popularity += 20 * forks + stars * 2 + watchers;
            if (repo.hasIssues()) {
                popularity *= 1.2;
            }
        }

        List<CommitActivity> lastTwoWeeks = new ArrayList<>();

        for (List<CommitActivity> activities : commitActivity.values()) {
            for (CommitActivity activity : activities) {
                if (Math.abs((activity.getWeek() * 1000) - System.currentTimeMillis()) < ONE_WEEK) {
                    lastTwoWeeks.add(activity);
                } else {
                    totalXp += activity.getTotalCommits();
                }
            }
        }
        Collections.sort(lastTwoWeeks, (lhs, rhs) -> (int)(rhs.getWeek() - lhs.getWeek()));
        for (CommitActivity activity : lastTwoWeeks) {
            totalXp += activity.getTotalCommits() * 100;
        }
        totalXp += popularity * 5;
        return new UserXp(totalXp);
    }
}
