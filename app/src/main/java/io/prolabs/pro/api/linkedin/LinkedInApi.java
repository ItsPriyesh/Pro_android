package io.prolabs.pro.api.linkedin;

import io.prolabs.pro.api.oauth.RetrofitHttpOAuthConsumer;
import io.prolabs.pro.api.oauth.SigningOkClient;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

public class LinkedInApi {

    private static LinkedInService linkedInService = null;

    private static final String BASE_URL = "https://api.linkedin.com/v1";

    public static LinkedInService getService() {
        if (linkedInService == null) {

            RetrofitHttpOAuthConsumer oAuthConsumer = new RetrofitHttpOAuthConsumer("785s7f2efkecb6", "BNpwWM0BCSObpPVE");
            oAuthConsumer.setTokenWithSecret(oAuthConsumer.getToken(), oAuthConsumer.getTokenSecret());
            OkClient client = new SigningOkClient(oAuthConsumer);

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setRequestInterceptor(request ->
                        request.addHeader("x-li-format", "json")
                    )
                    .setEndpoint(BASE_URL)
                    .setClient(client)
                    .build();
            linkedInService = restAdapter.create(LinkedInService.class);
        }
        return linkedInService;
    }
}
