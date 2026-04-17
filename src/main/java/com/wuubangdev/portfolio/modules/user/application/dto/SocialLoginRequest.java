package com.wuubangdev.portfolio.modules.user.application.dto;

import jakarta.validation.constraints.NotBlank;

public record SocialLoginRequest(
        @NotBlank(message = "Access token không được để trống")
        String accessToken
) {}
