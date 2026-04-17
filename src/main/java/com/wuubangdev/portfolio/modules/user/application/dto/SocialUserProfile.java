package com.wuubangdev.portfolio.modules.user.application.dto;

public record SocialUserProfile(
        String provider,
        String providerId,
        String email,
        String name,
        String avatarUrl
) {}
