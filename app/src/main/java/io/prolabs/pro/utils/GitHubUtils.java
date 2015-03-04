package io.prolabs.pro.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import io.prolabs.pro.models.github.Repo;

public class GitHubUtils {

    public static int getTotalStars(List<Repo> repos) {
        int totalStars = 0;
        for (Repo repo : repos) totalStars += repo.getStars();
        return totalStars;
    }

    public static List<String> getTopLanguages(List<Repo> repos) {
        HashMap<String, Integer> occurrences = new HashMap<>();
        for (Repo repo : repos) {
            String lang = repo.getLanguage();
            if (lang != null) {
                if (occurrences.containsKey(lang)) {
                    occurrences.put(lang, occurrences.get(lang) + 1);
                } else {
                    occurrences.put(lang, 1);
                }
            }
        }

        List<String> topLanguages = new ArrayList<>(occurrences.keySet());
        Collections.sort(topLanguages, (s1, s2) -> {
            Integer occurrences1 = occurrences.get(s1);
            Integer occurrences2 = occurrences.get(s2);
            return occurrences2.compareTo(occurrences1);
        });

        return topLanguages;
    }
}
