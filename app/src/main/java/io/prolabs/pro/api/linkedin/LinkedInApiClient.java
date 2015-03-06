package io.prolabs.pro.api.linkedin;

import org.scribe.builder.ServiceBuilder;
import org.scribe.oauth.OAuthService;

public class LinkedInApiClient {

    private static OAuthService linkedInService = null;

    private static final String BASE_URL = "https://api.linkedin.com/v1";
    private static final String API_KEY = "785s7f2efkecb6";
    private static final String API_SECRET = "BNpwWM0BCSObpPVE";

    public static OAuthService getService() {
        if (linkedInService == null) {
            linkedInService = new ServiceBuilder()
                    .provider(org.scribe.builder.api.LinkedInApi.class)
                    .apiKey(API_KEY)
                    .apiSecret(API_SECRET)
                    .build();
        }
        return linkedInService;
    }
}
