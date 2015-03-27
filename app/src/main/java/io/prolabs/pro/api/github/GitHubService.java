package io.prolabs.pro.api.github;

import com.google.gson.JsonElement;

import java.util.List;

import io.prolabs.pro.models.github.CommitActivity;
import io.prolabs.pro.models.github.Gist;
import io.prolabs.pro.models.github.GitHubUser;
import io.prolabs.pro.models.github.Limits;
import io.prolabs.pro.models.github.Repo;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface GitHubService {

    @GET("/rate_limit")
    void getRateLimit(Callback<Limits> callback);

    @GET("/user")
    Observable<GitHubUser> getAuthUser();

    @GET("/user/repos")
    void getRepos(@Query("per_page") int reposPerPage, Callback<List<Repo>> callback);

    @GET("/repos/{user}/{repo}/languages")
    void getLanguages(@Path("user") String user, @Path("repo") String repo, Callback<JsonElement> callback);

    @GET("/repos/{user}/{repo}/stats/commit_activity")
    void getCommitActivity(@Path("user") String user, @Path("repo") String repo, Callback<List<CommitActivity>> callback);

    @GET("/repos/{user}/{repo}/stats/code_frequency")
    void getCodeFrequency(@Path("user") String user, @Path("repo") String repo, Callback<JsonElement> callback);

    // Doesn't take a user; assumes the authenticated user.
    @GET("/gists/public")
    void getGists(Callback<List<Gist>> callback);

}