package com.wuubangdev.portfolio.modules.setting.application.dto;

public record SettingRequest(
        String logo,
        String thumbnailImageSeo,
        String titleSeo,
        String descriptionSeo
) {
}
