package io.prolabs.pro.api;

import java.util.List;

import io.prolabs.pro.models.github.Repo;
import io.prolabs.pro.models.github.User;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface GitHubService {

    @GET("/user")
    void getAuthUser(Callback<User> callback);

    @GET("/user/repos")
    void getRepos(@Query("per_page") int perPage, Callback<List<Repo>> callback);

}