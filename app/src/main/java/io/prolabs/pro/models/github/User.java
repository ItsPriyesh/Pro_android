package io.prolabs.pro.models.github;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("public_repos")
    private int publicReposCount;

    public int getPublicRepoCount() {
        return publicReposCount;
    }
}