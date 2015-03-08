package io.prolabs.pro.models.github;

import com.google.gson.annotations.SerializedName;

public class Repo {

    @SerializedName("stargazers_count")
    private int stars;

    @SerializedName("language")
    private String language;

    @SerializedName("name")
    private String name;

    @SerializedName("watchers_count")
    private long watchers;

    @SerializedName("private")
    private boolean isPrivate;

    @SerializedName("fork")
    private boolean isFork;

    @SerializedName("forks_count")
    private long forks;

    @SerializedName("has_issues")
    private boolean hasIssues;

    public boolean isFork() {
        return isFork;
    }

    public long getForks() {
        return forks;
    }

    public boolean hasIssues() {
        return hasIssues;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public long getWatchers() {
        return watchers;
    }

    public String getName() {
        return name;
    }

    public int getStars() {
        return stars;
    }

    public String getLanguage() {
        return language;
    }

    public int hashCode() {
        return name.hashCode();
    }

    public boolean equals(Object other) {
        if (other == null) return false;
        if (!(other instanceof Repo)) return false;
        Repo otherRepo = (Repo) other;
        return otherRepo.name.equals(this.name);
    }

}