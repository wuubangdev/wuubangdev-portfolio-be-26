package com.wuubangdev.portfolio.modules.social.application.dto;

public record SocialRequest(
        String facebook,
        String github,
        String linkedin,
        String zalo,
        String telegram,
        String gmail,
        String phone,
        String address,
        String addressGgMapLink
) {
}
