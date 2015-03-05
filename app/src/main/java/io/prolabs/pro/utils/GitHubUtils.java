package io.prolabs.pro.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.prolabs.pro.api.GitHubApi;
import io.prolabs.pro.api.GitHubService;
import io.prolabs.pro.models.github.Language;
import io.prolabs.pro.models.github.Repo;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

public class GitHubUtils {

    public static int getTotalStars(List<Repo> repos) {
        int totalStars = 0;
        for (Repo repo : repos) totalStars += repo.getStars();
        return totalStars;
    }

    public static List<Language> getLanguages(String username, List<Repo> repos) {
        GitHubService gitHubService = GitHubApi.getService();

        for (Repo repo : repos) {
            gitHubService.getLanguages(username, repo.getName(), new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    //languages = GitHubUtils.parseLanguageResponse(jsonElement);
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }
        Type type = new TypeToken<Map<String, Integer>>() {}.getType();
        Map<String, Integer> responseMap = new Gson().fromJson(json.toString(), type);

        List<Language> languages = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : responseMap.entrySet()) {
            languages.add(new Language(entry.getKey(), entry.getValue()));
        }

        Collections.sort(languages);
        return languages;
    }
}
