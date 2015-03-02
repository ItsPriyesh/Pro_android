package io.prolabs.pro.models.github;

import com.google.gson.annotations.SerializedName;

public class Repo {

    @SerializedName("stargazers_count")
    private int starCount;

    public int getStarCount() {
        return starCount;
    }

}