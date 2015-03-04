package io.prolabs.pro.models.github;

import com.google.gson.annotations.SerializedName;

public class Repo {

    @SerializedName("stargazers_count")
    private int stars;

    @SerializedName("language")
    private String language;

    public int getStars() {
        return stars;
    }

    public String getLanguage() {
        return language;
    }

}