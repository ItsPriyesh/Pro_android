package io.prolabs.pro.models.github;

import com.google.gson.annotations.SerializedName;

public class GitHubUser {

    @SerializedName("public_repos")
    private int publicReposCount;

    @SerializedName("total_private_repos")
    private int privateReposCount;

    @SerializedName("name")
    private String name;

    @SerializedName("login")
    private String username;

    @SerializedName("avatar_url")
    private String avatarUrl;

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public int getPublicRepoCount() {
        return publicReposCount;
    }

    public int getPrivateReposCount() {
        return privateReposCount;
    }
}