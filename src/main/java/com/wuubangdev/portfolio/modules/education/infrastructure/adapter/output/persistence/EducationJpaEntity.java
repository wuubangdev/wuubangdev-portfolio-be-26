package com.wuubangdev.portfolio.modules.education.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.infrastructure.persistence.base.BaseEntity;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "educations")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class EducationJpaEntity extends BaseEntity {
    private String institution;
    private String degree;
    private String fieldOfStudy;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private String location;
    private Integer displayOrder;
    private Boolean isPublic;
}
