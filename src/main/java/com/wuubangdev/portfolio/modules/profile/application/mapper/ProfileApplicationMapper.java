package com.wuubangdev.portfolio.modules.profile.application.mapper;

import com.wuubangdev.portfolio.modules.profile.application.dto.ProfileRequest;
import com.wuubangdev.portfolio.modules.profile.application.dto.ProfileResponse;
import com.wuubangdev.portfolio.modules.profile.domain.model.Profile;
import com.wuubangdev.portfolio.modules.profile.domain.model.SocialLink;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class ProfileApplicationMapper {

    public ProfileResponse toResponse(Profile profile) {
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
                links
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
        return new ProfileResponse(null, "wuubangdev", "Full Stack Developer", "", "", "", "", "", "", List.of());
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
}
