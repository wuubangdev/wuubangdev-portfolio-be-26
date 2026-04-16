package com.wuubangdev.portfolio.modules.language.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.infrastructure.persistence.base.BaseEntity;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "languages")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LanguageJpaEntity extends BaseEntity {
    @Indexed(unique = true)
    private String code;
    private String name;
    private Boolean isDefault;
}
