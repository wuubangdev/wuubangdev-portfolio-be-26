package com.wuubangdev.portfolio.modules.profile.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.modules.profile.domain.model.Profile;
import com.wuubangdev.portfolio.modules.profile.domain.model.SocialLink;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class ProfileMapper {

    public Profile toDomain(ProfileJpaEntity entity) {
        List<SocialLink> links = entity.getSocialLinks() != null
                ? entity.getSocialLinks().stream()
                    .map(e -> SocialLink.builder().platform(e.getPlatform()).url(e.getUrl()).icon(e.getIcon()).build())
                    .toList()
                : Collections.emptyList();

        return Profile.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .title(entity.getTitle())
                .bio(entity.getBio())
                .avatarUrl(entity.getAvatarUrl())
                .resumeUrl(entity.getResumeUrl())
                .location(entity.getLocation())
                .email(entity.getEmail())
                .socialLinks(links)
                .build();
    }

    public ProfileJpaEntity toEntity(Profile domain) {
        List<SocialLinkEmbeddable> links = domain.getSocialLinks() != null
                ? domain.getSocialLinks().stream()
                    .map(sl -> SocialLinkEmbeddable.builder().platform(sl.getPlatform()).url(sl.getUrl()).icon(sl.getIcon()).build())
                    .toList()
                : Collections.emptyList();

        ProfileJpaEntity entity = new ProfileJpaEntity();
        entity.setId(domain.getId());
        entity.setFullName(domain.getFullName());
        entity.setTitle(domain.getTitle());
        entity.setBio(domain.getBio());
        entity.setAvatarUrl(domain.getAvatarUrl());
        entity.setResumeUrl(domain.getResumeUrl());
        entity.setLocation(domain.getLocation());
        entity.setEmail(domain.getEmail());
        entity.setSocialLinks(links);
        return entity;
    }
}
