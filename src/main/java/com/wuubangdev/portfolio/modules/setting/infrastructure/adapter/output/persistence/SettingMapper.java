package com.wuubangdev.portfolio.modules.setting.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.modules.setting.domain.model.Setting;
import org.springframework.stereotype.Component;

@Component
public class SettingMapper {

    public Setting toDomain(SettingJpaEntity entity) {
        return Setting.builder()
                .id(entity.getId())
                .logo(entity.getLogo())
                .thumbnailImageSeo(entity.getThumbnailImageSeo())
                .titleSeo(entity.getTitleSeo())
                .descriptionSeo(entity.getDescriptionSeo())
                .build();
    }

    public SettingJpaEntity toEntity(Setting domain) {
        SettingJpaEntity entity = new SettingJpaEntity();
        entity.setId(domain.getId());
        entity.setLogo(domain.getLogo());
        entity.setThumbnailImageSeo(domain.getThumbnailImageSeo());
        entity.setTitleSeo(domain.getTitleSeo());
        entity.setDescriptionSeo(domain.getDescriptionSeo());
        return entity;
    }
}
