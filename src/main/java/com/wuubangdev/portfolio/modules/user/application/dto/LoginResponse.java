package com.wuubangdev.portfolio.modules.user.application.dto;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        String tokenType, // Giá trị thường là "Bearer"
        Long expiresIn,
        Long refreshExpiresIn,
        String message
) {}
