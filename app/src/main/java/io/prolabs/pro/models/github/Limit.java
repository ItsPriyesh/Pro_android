package io.prolabs.pro.models.github;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Edmund on 2015-03-08.
 */
public class Limit {
    @SerializedName("limit")
    private long limit;

    public long getLimit() {
        return limit;
    }

    public long getRemaining() {
        return remaining;
    }

    public long getReset() {
        return reset;
    }

    @SerializedName("remaining")
    private long remaining;

    @SerializedName("reset")
    private long reset;
}
