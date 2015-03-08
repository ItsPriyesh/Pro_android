package io.prolabs.pro.models.github;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Edmund on 2015-03-08.
 */
public class ResourceLimit {
    @SerializedName("core")
    private Limit coreLimit;

    @SerializedName("search")
    private Limit searchLimit;
}
