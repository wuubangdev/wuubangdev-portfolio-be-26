package com.wuubangdev.portfolio.modules.post.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.infrastructure.persistence.base.BaseEntity;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "categories")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CategoryJpaEntity extends BaseEntity {
    private String name;

    @Indexed(unique = true)
    private String slug;

    private String description;
}
