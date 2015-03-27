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
    Observable<List<Repo>> getRepos(@Query("per_page") int reposPerPage);

    @GET("/repos/{user}/{repo}/languages")
    Observable<JsonElement> getLanguages(@Path("user") String user, @Path("repo") String repo);

    @GET("/repos/{user}/{repo}/stats/commit_activity")
    Observable<List<CommitActivity>> getCommitActivity(@Path("user") String user, @Path("repo") String repo);

    @GET("/repos/{user}/{repo}/stats/code_frequency")
    Observable<JsonElement> getCodeFrequency(@Path("user") String user, @Path("repo") String repo);

    // Doesn't take a user; assumes the authenticated user.
    @GET("/gists/public")
    Observable<List<Gist>> getGists();

}