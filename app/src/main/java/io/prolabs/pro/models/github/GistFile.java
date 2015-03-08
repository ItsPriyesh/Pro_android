package io.prolabs.pro.models.github;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Edmund on 2015-03-07.
 */
public class GistFile {
    public long getSizeInBytes() {
        return sizeInBytes;
    }

    public String getLanguage() {
        return language;
    }

    public GistFile(long sizeInBytes, String language) {
        this.sizeInBytes = sizeInBytes;
        this.language = language;
    }

    @SerializedName("size")
    private long sizeInBytes;

    @SerializedName("language")
    private String language;
}
