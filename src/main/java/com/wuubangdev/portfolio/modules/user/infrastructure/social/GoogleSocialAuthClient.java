package com.wuubangdev.portfolio.modules.user.infrastructure.social;

import com.wuubangdev.portfolio.infrastructure.global.exception.BusinessException;
import com.wuubangdev.portfolio.modules.user.application.dto.SocialUserProfile;
import com.wuubangdev.portfolio.modules.user.application.port.SocialAuthClient;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class GoogleSocialAuthClient implements SocialAuthClient {

    private static final String GOOGLE_USERINFO_URL = "https://www.googleapis.com/oauth2/v3/userinfo";

    private final RestClient restClient = RestClient.create();

    @Override
    public SocialUserProfile fetchUserProfile(String accessToken) {
        try {
            GoogleUserInfoResponse response = restClient.get()
                    .uri(GOOGLE_USERINFO_URL)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .body(GoogleUserInfoResponse.class);

            if (response == null || response.sub() == null || response.email() == null) {
                throw new BusinessException("Unable to fetch Google profile");
            }

            return new SocialUserProfile(
                    "GOOGLE",
                    response.sub(),
                    response.email(),
                    response.name(),
                    response.picture()
            );
        } catch (RestClientException ex) {
            throw new BusinessException("Google access token is invalid");
        }
    }

    private record GoogleUserInfoResponse(
            String sub,
            String email,
            String name,
            String picture
    ) {}
}
