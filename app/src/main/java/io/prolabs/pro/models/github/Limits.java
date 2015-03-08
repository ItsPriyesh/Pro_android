package io.prolabs.pro.models.github;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Edmund on 2015-03-08.
 */
public class Limits {
    public ResourceLimit getResources() {
        return resources;
    }

    public Limit getRate() {
        return rate;
    }

    @SerializedName("resources")
    private ResourceLimit resources;

    @SerializedName("rate")
    private Limit rate;
}
