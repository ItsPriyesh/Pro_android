package io.prolabs.pro.models.github;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CommitActivity {

    @SerializedName("days")
    private List<Integer> commitsPerDay = new ArrayList<>();

    @SerializedName("total")
    private int totalCommits;

    @SerializedName("week")
    private long week;

    public long getWeek() {
        return week;
    }

    public List<Integer> getCommitsPerDay() {
        return commitsPerDay;
    }

    public int getTotalCommits() {
        return totalCommits;
    }
}
