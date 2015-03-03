package io.prolabs.pro.api;

import android.util.Base64;

import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

public class GitHubApi {
    private static GitHubService gitHubService;

    private static final String BASE_URL = "https://api.github.com";
    private static final String ACCEPT_HEADER_NAME = "Accept";
    private static final String ACCEPT_HEADER_VALUE = "application/vnd.github.v3+json";
    private static final String AUTH_HEADER_NAME = "Authorization";

    public static GitHubService getService(String username, String password) {
        if (gitHubService == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setClient(new OkClient(new OkHttpClient()))
                    .setRequestInterceptor(request -> {
                        request.addHeader(ACCEPT_HEADER_NAME, ACCEPT_HEADER_VALUE);
                        request.addHeader(AUTH_HEADER_NAME, generateAuthValue(username, password));
                    })
                    .setEndpoint(BASE_URL)
                    .build();

            gitHubService = restAdapter.create(GitHubService.class);
        }
        return gitHubService;
    }

    private static String generateAuthValue(String username, String password) {
        return "Basic " + Base64.encodeToString(
                String.format("%s:%s", username, password).getBytes(), Base64.NO_WRAP);
    }
}
