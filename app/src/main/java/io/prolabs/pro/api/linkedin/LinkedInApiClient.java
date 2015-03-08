package io.prolabs.pro.api.linkedin;

import org.scribe.builder.ServiceBuilder;
import org.scribe.oauth.OAuthService;

import io.prolabs.pro.SecretStuff;

public class LinkedInApiClient {

    private static OAuthService linkedInService = null;

    public static OAuthService getService() {
        if (linkedInService == null) {
            linkedInService = new ServiceBuilder()
                    .provider(org.scribe.builder.api.LinkedInApi.class)
                    .apiKey(SecretStuff.LINKEDIN_API_KEY)
                    .apiSecret(SecretStuff.LINKEDIN_API_SECRET)
                    .build();
        }
        return linkedInService;
    }
}
