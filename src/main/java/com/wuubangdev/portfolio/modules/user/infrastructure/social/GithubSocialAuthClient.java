package com.wuubangdev.portfolio.modules.user.infrastructure.social;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wuubangdev.portfolio.infrastructure.global.exception.BusinessException;
import com.wuubangdev.portfolio.modules.user.application.dto.SocialUserProfile;
import com.wuubangdev.portfolio.modules.user.application.port.SocialAuthClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Arrays;

@Component
public class GithubSocialAuthClient implements SocialAuthClient {

    private static final String GITHUB_USER_URL = "https://api.github.com/user";
    private static final String GITHUB_EMAILS_URL = "https://api.github.com/user/emails";

    private final RestClient restClient = RestClient.create();

    @Override
    public SocialUserProfile fetchUserProfile(String accessToken) {
        try {
            GithubUserResponse user = restClient.get()
                    .uri(GITHUB_USER_URL)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.USER_AGENT, "wuubangdev-portfolio")
                    .retrieve()
                    .body(GithubUserResponse.class);

            if (user == null || user.id() == null) {
                throw new BusinessException("Unable to fetch GitHub profile");
            }

            String email = user.email();
            if (email == null || email.isBlank()) {
                GithubEmailResponse[] emails = restClient.get()
                        .uri(GITHUB_EMAILS_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.USER_AGENT, "wuubangdev-portfolio")
                        .retrieve()
                        .body(GithubEmailResponse[].class);
                email = pickGithubEmail(emails);
            }

            if (email == null || email.isBlank()) {
                throw new BusinessException("GitHub account does not expose a usable email");
            }

            return new SocialUserProfile(
                    "GITHUB",
                    String.valueOf(user.id()),
                    email,
                    user.name() != null && !user.name().isBlank() ? user.name() : user.login(),
                    user.avatarUrl()
            );
        } catch (RestClientException ex) {
            throw new BusinessException("GitHub access token is invalid");
        }
    }

    private String pickGithubEmail(GithubEmailResponse[] emails) {
        if (emails == null || emails.length == 0) {
            return null;
        }
        return Arrays.stream(emails)
                .filter(email -> Boolean.TRUE.equals(email.verified()))
                .sorted((a, b) -> Boolean.compare(Boolean.TRUE.equals(b.primary()), Boolean.TRUE.equals(a.primary())))
                .map(GithubEmailResponse::email)
                .filter(value -> value != null && !value.isBlank())
                .findFirst()
                .orElse(null);
    }

    private record GithubUserResponse(
            Long id,
            String login,
            String name,
            String email,
            @JsonProperty("avatar_url")
            String avatarUrl
    ) {}

    private record GithubEmailResponse(
            String email,
            Boolean primary,
            Boolean verified
    ) {}
}
