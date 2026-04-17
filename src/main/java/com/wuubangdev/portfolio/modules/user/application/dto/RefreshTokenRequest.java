package com.wuubangdev.portfolio.modules.user.application.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @NotBlank(message = "Refresh token không được để trống")
        String refreshToken
) {}
