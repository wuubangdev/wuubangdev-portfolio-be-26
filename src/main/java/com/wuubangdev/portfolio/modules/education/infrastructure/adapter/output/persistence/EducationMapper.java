package com.wuubangdev.portfolio.modules.education.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.modules.education.domain.model.Education;
import org.springframework.stereotype.Component;

@Component
public class EducationMapper {

    public Education toDomain(EducationJpaEntity entity) {
        return Education.builder()
                .id(entity.getId())
                .institution(entity.getInstitution())
                .degree(entity.getDegree())
                .fieldOfStudy(entity.getFieldOfStudy())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .description(entity.getDescription())
                .location(entity.getLocation())
                .displayOrder(entity.getDisplayOrder())
                .isPublic(entity.getIsPublic())
                .build();
    }

    public EducationJpaEntity toEntity(Education domain) {
        EducationJpaEntity entity = new EducationJpaEntity();
        entity.setId(domain.getId());
        entity.setInstitution(domain.getInstitution());
        entity.setDegree(domain.getDegree());
        entity.setFieldOfStudy(domain.getFieldOfStudy());
        entity.setStartDate(domain.getStartDate());
        entity.setEndDate(domain.getEndDate());
        entity.setDescription(domain.getDescription());
        entity.setLocation(domain.getLocation());
        entity.setDisplayOrder(domain.getDisplayOrder());
        entity.setIsPublic(domain.getIsPublic());
        return entity;
    }
}
