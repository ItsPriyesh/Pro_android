package io.prolabs.pro.api.linkedin;

import io.prolabs.pro.models.linkedin.User;
import retrofit.Callback;
import retrofit.http.GET;

public interface LinkedInService {
    @GET("/people/~")
    void getProfile(Callback<User> callback);
}
