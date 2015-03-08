package io.prolabs.pro.models.github;

import java.util.Date;

/**
 * Created by Edmund on 2015-03-07.
 */
public class CodeWeek {
    private Date weekStart;
    private long addedLines;
    private long deletedLines;

    public CodeWeek(Date weekStart, long addedLines, long deletedLines) {
        this.weekStart = weekStart;
        this.addedLines = addedLines;
        this.deletedLines = deletedLines;
    }

    public long getAddedLines() {
        return addedLines;
    }

    public Date getWeekStart() {
        return weekStart;
    }

    public long getDeletedLines() {
        return deletedLines;
    }
}
