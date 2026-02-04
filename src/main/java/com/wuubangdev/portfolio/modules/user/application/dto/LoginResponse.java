package com.wuubangdev.portfolio.modules.user.application.dto;

public record LoginResponse(
        String accessToken,
        String tokenType // Giá trị thường là "Bearer"
) {}