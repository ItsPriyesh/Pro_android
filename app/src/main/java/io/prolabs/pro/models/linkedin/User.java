package io.prolabs.pro.models.linkedin;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("firstName")
    private String firstName;

    public String getFirstName() {
        return firstName;
    }
}
