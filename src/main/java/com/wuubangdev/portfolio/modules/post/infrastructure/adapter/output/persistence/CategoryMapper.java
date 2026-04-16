package com.wuubangdev.portfolio.modules.post.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.modules.post.domain.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toDomain(CategoryJpaEntity entity) {
        return Category.builder()
                .id(entity.getId())
                .name(entity.getName())
                .slug(entity.getSlug())
                .description(entity.getDescription())
                .build();
    }

    public CategoryJpaEntity toEntity(Category domain) {
        CategoryJpaEntity entity = new CategoryJpaEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setSlug(domain.getSlug());
        entity.setDescription(domain.getDescription());
        return entity;
    }
}
