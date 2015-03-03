package io.prolabs.pro.models.github;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("public_repos")
    private int publicReposCount;

    @SerializedName("total_private_repos")
    private int privateReposCount;

    public int getPublicRepoCount() {
        return publicReposCount;
    }

    public int getPrivateReposCount() {
        return privateReposCount;
    }
}