package io.prolabs.pro.api;

import java.util.List;

import io.prolabs.pro.models.github.Repo;
import io.prolabs.pro.models.github.User;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;

public class GitHubApi {
    private static GitHubService gitHubService;

    private static final String BASE_URL = "https://api.github.com";
    private static final String HEADER_NAME = "Accept";
    private static final String HEADER_VALUE = "application/vnd.github.v3+json";

    public static GitHubService getService() {
        if (gitHubService == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setRequestInterceptor(request -> request.addHeader(HEADER_NAME, HEADER_VALUE))
                    .setEndpoint(BASE_URL)
                    .build();

            gitHubService = restAdapter.create(GitHubService.class);
        }

        return gitHubService;
    }

    public interface GitHubService {

        @GET("/users/{user}")
        void getUser(@Path("user") String user, Callback<User> callback);

        @GET("/users/{user}/repos")
        void getRepoList(@Path("user") String user, Callback<List<Repo>> callback);
    }
}
