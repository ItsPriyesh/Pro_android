package io.prolabs.pro.models.github;

import android.support.annotation.NonNull;

public class Language implements Comparable<Language> {
    private String name;
    private int bytes;

    public Language(String name, int bytes) {
        this.name = name;
        this.bytes = bytes;
    }

    public String getName() {
        return name;
    }

    public int getBytes() {
        return bytes;
    }

    @Override
    public int compareTo(Language language) {
        return language.bytes - this.bytes;
    }

    public boolean equals(@NonNull Language other) {
        return name.equals(other.name);
    }


}
