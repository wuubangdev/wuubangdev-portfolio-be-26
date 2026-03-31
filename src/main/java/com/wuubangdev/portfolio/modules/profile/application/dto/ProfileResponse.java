package com.wuubangdev.portfolio.modules.profile.application.dto;

import java.util.List;

public record ProfileResponse(
        Long id,
        String fullName,
        String title,
        String bio,
        String avatarUrl,
        String resumeUrl,
        String location,
        String email,
        String phone,
        List<SocialLinkDto> socialLinks
) {
    public record SocialLinkDto(String platform, String url, String icon) {}
}
