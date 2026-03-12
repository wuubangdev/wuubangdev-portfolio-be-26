package com.wuubangdev.portfolio.modules.profile.application.service;

import com.wuubangdev.portfolio.modules.profile.application.dto.ProfileRequest;
import com.wuubangdev.portfolio.modules.profile.application.dto.ProfileResponse;
import com.wuubangdev.portfolio.modules.profile.domain.model.Profile;
import com.wuubangdev.portfolio.modules.profile.domain.model.SocialLink;
import com.wuubangdev.portfolio.modules.profile.domain.port.ProfileRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepositoryPort profileRepositoryPort;

    @Override
    public ProfileResponse getProfile() {
        return profileRepositoryPort.findFirst()
                .map(this::toResponse)
                .orElseGet(() -> new ProfileResponse(null, "wuubangdev", "Full Stack Developer", "", "", "", "", "", List.of()));
    }

    @Override
    @Transactional
    public ProfileResponse upsertProfile(ProfileRequest request) {
        Profile profile = profileRepositoryPort.findFirst()
                .orElseGet(Profile::new);

        profile.setFullName(request.fullName());
        profile.setTitle(request.title());
        profile.setBio(request.bio());
        profile.setAvatarUrl(request.avatarUrl());
        profile.setResumeUrl(request.resumeUrl());
        profile.setLocation(request.location());
        profile.setEmail(request.email());

        List<SocialLink> socialLinks = request.socialLinks() != null
                ? request.socialLinks().stream()
                    .map(dto -> SocialLink.builder()
                            .platform(dto.platform())
                            .url(dto.url())
                            .icon(dto.icon())
                            .build())
                    .toList()
                : Collections.emptyList();
        profile.setSocialLinks(socialLinks);

        return toResponse(profileRepositoryPort.save(profile));
    }

    private ProfileResponse toResponse(Profile p) {
        List<ProfileResponse.SocialLinkDto> links = p.getSocialLinks() != null
                ? p.getSocialLinks().stream()
                    .map(sl -> new ProfileResponse.SocialLinkDto(sl.getPlatform(), sl.getUrl(), sl.getIcon()))
                    .toList()
                : List.of();
        return new ProfileResponse(p.getId(), p.getFullName(), p.getTitle(), p.getBio(),
                p.getAvatarUrl(), p.getResumeUrl(), p.getLocation(), p.getEmail(), links);
    }
}
