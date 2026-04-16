package com.wuubangdev.portfolio.modules.skill.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.infrastructure.persistence.base.BaseEntity;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "skills")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SkillJpaEntity extends BaseEntity {
    private String name;

    private String category;
    private Integer level;
    private String icon;
    private Integer displayOrder;
    private Boolean isHidden;
}
