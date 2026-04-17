package com.wuubangdev.portfolio.modules.profile.application.mapper;

import com.wuubangdev.portfolio.modules.profile.application.dto.ProfileRequest;
import com.wuubangdev.portfolio.modules.profile.application.dto.ProfileResponse;
import com.wuubangdev.portfolio.modules.profile.domain.model.Profile;
import com.wuubangdev.portfolio.modules.profile.domain.model.ProfileTranslation;
import com.wuubangdev.portfolio.modules.profile.domain.model.SocialLink;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class ProfileApplicationMapper {

    public ProfileResponse toResponse(Profile profile) {
        return toResponse(profile, null, false);
    }

    public ProfileResponse toResponse(Profile profile, String locale) {
        return toResponse(profile, locale, false);
    }

    public ProfileResponse toResponse(Profile profile, String locale, boolean translated) {
        List<ProfileResponse.SocialLinkDto> links = profile.getSocialLinks() != null
                ? profile.getSocialLinks().stream()
                .map(link -> new ProfileResponse.SocialLinkDto(link.getPlatform(), link.getUrl(), link.getIcon()))
                .toList()
                : List.of();

        return new ProfileResponse(
                profile.getId(),
                profile.getFullName(),
                profile.getTitle(),
                profile.getBio(),
                profile.getAvatarUrl(),
                profile.getResumeUrl(),
                profile.getLocation(),
                profile.getEmail(),
                profile.getPhone(),
                links,
                locale,
                translated
        );
    }

    public Profile toDomain(ProfileRequest request, Profile profile) {
        profile.setFullName(request.fullName());
        profile.setTitle(request.title());
        profile.setBio(request.bio());
        profile.setAvatarUrl(request.avatarUrl());
        profile.setResumeUrl(request.resumeUrl());
        profile.setLocation(request.location());
        profile.setEmail(request.email());
        profile.setPhone(request.phone());
        profile.setSocialLinks(toSocialLinks(request.socialLinks()));
        return profile;
    }

    public ProfileResponse defaultResponse() {
        return defaultResponse(null);
    }

    public ProfileResponse defaultResponse(String locale) {
        return new ProfileResponse(null, "wuubangdev", "Full Stack Developer", "", "", "", "", "", "", List.of(), locale, false);
    }

    public Profile applyTranslation(Profile profile, ProfileTranslation translation) {
        if (translation == null) {
            return profile;
        }

        return Profile.builder()
                .id(profile.getId())
                .fullName(defaultIfBlank(translation.getFullName(), profile.getFullName()))
                .title(defaultIfBlank(translation.getTitle(), profile.getTitle()))
                .bio(defaultIfBlank(translation.getBio(), profile.getBio()))
                .avatarUrl(profile.getAvatarUrl())
                .resumeUrl(profile.getResumeUrl())
                .location(defaultIfBlank(translation.getLocation(), profile.getLocation()))
                .email(profile.getEmail())
                .phone(profile.getPhone())
                .socialLinks(profile.getSocialLinks())
                .build();
    }

    private List<SocialLink> toSocialLinks(List<ProfileRequest.SocialLinkDto> socialLinks) {
        if (socialLinks == null) {
            return Collections.emptyList();
        }

        return socialLinks.stream()
                .map(link -> SocialLink.builder()
                        .platform(link.platform())
                        .url(link.url())
                        .icon(link.icon())
                .build())
                .toList();
    }

    private String defaultIfBlank(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
