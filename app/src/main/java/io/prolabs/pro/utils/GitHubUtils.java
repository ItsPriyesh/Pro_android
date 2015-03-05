package io.prolabs.pro.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.prolabs.pro.models.github.Language;
import io.prolabs.pro.models.github.Repo;

public class GitHubUtils {

    public static int getTotalStars(List<Repo> repos) {
        int totalStars = 0;
        for (Repo repo : repos) totalStars += repo.getStars();
        return totalStars;
    }

    public static List<Language> parseLanguageResponse(JsonElement json) {
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
