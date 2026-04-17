package com.wuubangdev.portfolio.modules.post.application.dto;

public record PostEngagementResponse(
        Long postId,
        String slug,
        Integer likes,
        Integer shares
) {}
