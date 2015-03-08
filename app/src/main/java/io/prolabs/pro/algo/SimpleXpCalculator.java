package io.prolabs.pro.algo;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.prolabs.pro.models.github.CodeWeek;
import io.prolabs.pro.models.github.CommitActivity;
import io.prolabs.pro.models.github.Language;
import io.prolabs.pro.models.github.Repo;

/**
 * Created by Edmund on 2015-03-08.
 */
public class SimpleXpCalculator implements XpCalculator {

    private static final double maxDays = 730.0;

    @Override
    public UserXp calculateXp(FullUserStats stats) {
        //CommitActivity commitActivity = stats.getCommits();
        Map<Repo, List<Language>> languagesByRepo = stats.getLanguagesByRepo();
        Map<Repo, List<CodeWeek>> codeWeeks = stats.getWeeksOfCodeByRepo();
        List<Repo> repos = stats.getRepos();
        long totalXp = 0;
        Date now = new Date();

        for (List<CodeWeek> weeks : codeWeeks.values()) {
            for (CodeWeek week : weeks) {
                Date date = week.getWeekStart();
                long added = week.getAddedLines();
                long deleted = week.getDeletedLines();
                long timeDiff = now.getTime() - date.getTime();
                long dayDiff = TimeUnit.MILLISECONDS.toDays(timeDiff);
                totalXp += (long) ((added + deleted) * Math.exp(dayDiff - maxDays));
            }
        }
        return new UserXp(totalXp);
    }
}
