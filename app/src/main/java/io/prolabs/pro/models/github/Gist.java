package io.prolabs.pro.models.github;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by Edmund on 2015-03-07.
 */
public class Gist {
    @SerializedName("url")
    private String url;

    @SerializedName("id")
    private long id;

    public String getUrl() {
        return url;
    }

    public long getId() {
        return id;
    }

    public long getComments() {
        return comments;
    }

    public Map<String, GistFile> getFiles() {
        return files;
    }

    public Gist(String url, long id, long comments, Map<String, GistFile> files) {

        this.url = url;
        this.id = id;
        this.comments = comments;
        this.files = files;
    }

    @SerializedName("comments")
    private long comments;

    @SerializedName("files")
    private Map<String, GistFile> files;

}
