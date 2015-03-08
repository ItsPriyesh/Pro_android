package io.prolabs.pro.api.github;

import android.util.Base64;

import com.orhanobut.hawk.Hawk;
import com.squareup.okhttp.OkHttpClient;

import io.prolabs.pro.ProApp;
import io.prolabs.pro.models.github.GitHubUser;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

public class GitHubApi {

    public static final int MAX_REPOS_PER_PAGE = 100;
    private static final String BASE_URL = "https://api.github.com";
    private static final String ACCEPT_HEADER_NAME = "Accept";
    private static final String ACCEPT_HEADER_VALUE = "application/vnd.github.v3+json";
    private static final String AUTH_HEADER_NAME = "Authorization";
    private static GitHubService gitHubService = null;
    private static String AUTH_HEADER_VALUE;
    private static GitHubUser currentUser;

    public static GitHubService getService(String username, String password) {
        if (gitHubService == null) {
            generateAuthValue(username, password);

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setClient(new OkClient(new OkHttpClient()))
                    .setRequestInterceptor(request -> {
                        request.addHeader(ACCEPT_HEADER_NAME, ACCEPT_HEADER_VALUE);
                        request.addHeader(AUTH_HEADER_NAME, AUTH_HEADER_VALUE);
                    })
                    .setEndpoint(BASE_URL)
                    .build();

            gitHubService = restAdapter.create(GitHubService.class);
        }
        return gitHubService;
    }

    public static GitHubService getService(String authKey) {
        if (gitHubService == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setClient(new OkClient(new OkHttpClient()))
                    .setRequestInterceptor(request -> {
                        request.addHeader(ACCEPT_HEADER_NAME, ACCEPT_HEADER_VALUE);
                        request.addHeader(AUTH_HEADER_NAME, authKey);
                    })
                    .setEndpoint(BASE_URL)
                    .build();

            gitHubService = restAdapter.create(GitHubService.class);
        }
        return gitHubService;
    }

    public static GitHubService getService() {
        if (gitHubService != null) return gitHubService;
        else throw new IllegalAccessError("An authorized GitHub service has not been created");
    }

    private static String generateAuthValue(String username, String password) {
        AUTH_HEADER_VALUE = "Basic " + Base64.encodeToString(
                String.format("%s:%s", username, password).getBytes(), Base64.NO_WRAP);
        return AUTH_HEADER_VALUE;
    }

    public static GitHubUser getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(GitHubUser user) {
        currentUser = user;
    }

    public static void saveCurrentAuth(GitHubUser user) {
        setCurrentUser(user);
        Hawk.put(ProApp.GITHUB_USER, user);
        Hawk.put(ProApp.GITHUB_AUTH_KEY, AUTH_HEADER_VALUE);
    }

    public static void clearCurrentAuth() {
        Hawk.remove(ProApp.GITHUB_AUTH_KEY);
    }
}