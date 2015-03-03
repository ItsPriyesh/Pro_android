package io.prolabs.pro.api;

import java.util.List;

import io.prolabs.pro.models.github.Repo;
import io.prolabs.pro.models.github.User;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

public interface GitHubService {

    @GET("/users/{user}")
    void getUser(@Path("user") String user, Callback<User> callback);

    @GET("/users/{user}/repos")
    void getRepoList(@Path("user") String user, Callback<List<Repo>> callback);

    @GET("/user")
    void getAuthUser(Callback<User> callback);

   // @GET("users/{user}/{repo}/languages")
}