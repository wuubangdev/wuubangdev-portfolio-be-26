package com.wuubangdev.portfolio.modules.setting.application.dto;

public record SettingResponse(
        Long id,
        String logo,
        String thumbnailImageSeo,
        String titleSeo,
        String descriptionSeo
) {
}
