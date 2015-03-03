package io.prolabs.pro.api;

import android.util.Base64;

import com.orhanobut.hawk.Hawk;
import com.squareup.okhttp.OkHttpClient;

import io.prolabs.pro.ProApp;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

public class GitHubApi {

    private static GitHubService gitHubService = null;
    private static String AUTH_HEADER_VALUE;

    private static final String BASE_URL = "https://api.github.com";
    private static final String ACCEPT_HEADER_NAME = "Accept";
    private static final String ACCEPT_HEADER_VALUE = "application/vnd.github.v3+json";
    private static final String AUTH_HEADER_NAME = "Authorization";

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

    public static void saveCurrentAuth() {
        Hawk.put(ProApp.AUTH_KEY, AUTH_HEADER_VALUE);
    }

    public static void clearCurrentAuth() {
        Hawk.remove(ProApp.AUTH_KEY);
    }
}