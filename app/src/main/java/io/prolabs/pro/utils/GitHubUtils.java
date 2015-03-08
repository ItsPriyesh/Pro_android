package io.prolabs.pro.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.prolabs.pro.models.github.CodeWeek;
import io.prolabs.pro.models.github.Language;
import io.prolabs.pro.models.github.Repo;

public class GitHubUtils {

    public static int getTotalStars(List<Repo> repos) {
        int totalStars = 0;
        for (Repo repo : repos) totalStars += repo.getStars();
        return totalStars;
    }

    public static List<Language> parseLanguageResponse(JsonElement json) {
        Type type = new TypeToken<Map<String, Integer>>() {
        }.getType();
        Map<String, Integer> responseMap = new Gson().fromJson(json.toString(), type);

        List<Language> languages = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : responseMap.entrySet()) {
            languages.add(new Language(entry.getKey(), entry.getValue()));
        }

        Collections.sort(languages);
        return languages;
    }

    public static List<CodeWeek> parseCodeFrequencyResponse(JsonElement json) {
        ArrayList<CodeWeek> weekList = new ArrayList<>();
        JsonArray arrayOfWeeks = json.getAsJsonArray();
        for (JsonElement elem : arrayOfWeeks) {
            JsonArray week = elem.getAsJsonArray();
            long time = week.get(0).getAsLong();
            long added = week.get(1).getAsLong();
            long deleted = week.get(2).getAsLong();
            Date weekStart = new Date(time);
            CodeWeek codeWeek = new CodeWeek(weekStart, added, deleted);
            weekList.add(codeWeek);
        }
        return weekList;
    }

}
