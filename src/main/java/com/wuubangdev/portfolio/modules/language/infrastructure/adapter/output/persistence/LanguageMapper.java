package com.wuubangdev.portfolio.modules.language.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.modules.language.domain.model.Language;
import org.springframework.stereotype.Component;

@Component
public class LanguageMapper {

    public Language toDomain(LanguageJpaEntity entity) {
        return Language.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .isDefault(entity.getIsDefault())
                .build();
    }

    public LanguageJpaEntity toEntity(Language domain) {
        LanguageJpaEntity entity = new LanguageJpaEntity();
        entity.setId(domain.getId());
        entity.setCode(domain.getCode());
        entity.setName(domain.getName());
        entity.setIsDefault(domain.getIsDefault());
        return entity;
    }
}
