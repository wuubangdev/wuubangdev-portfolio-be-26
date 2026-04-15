package com.wuubangdev.portfolio.modules.experience.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.infrastructure.persistence.base.BaseEntity;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "experiences")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ExperienceJpaEntity extends BaseEntity {
    private String company;
    private String companyUrl;
    private String role;

    private String description;

    private String logoUrl;
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;
    private Integer displayOrder;
}
