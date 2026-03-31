package com.wuubangdev.portfolio.modules.experience.application.mapper;

import com.wuubangdev.portfolio.modules.experience.application.dto.ExperienceRequest;
import com.wuubangdev.portfolio.modules.experience.application.dto.ExperienceResponse;
import com.wuubangdev.portfolio.modules.experience.domain.model.Experience;
import org.springframework.stereotype.Component;

@Component
public class ExperienceApplicationMapper {

    public Experience toNewDomain(ExperienceRequest request) {
        return Experience.builder()
                .company(request.company())
                .companyUrl(request.companyUrl())
                .role(request.role())
                .description(request.description())
                .logoUrl(request.logoUrl())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .location(request.location())
                .displayOrder(request.displayOrder())
                .build();
    }

    public Experience updateDomain(Experience experience, ExperienceRequest request) {
        experience.setCompany(request.company());
        experience.setCompanyUrl(request.companyUrl());
        experience.setRole(request.role());
        experience.setDescription(request.description());
        experience.setLogoUrl(request.logoUrl());
        experience.setStartDate(request.startDate());
        experience.setEndDate(request.endDate());
        experience.setLocation(request.location());
        experience.setDisplayOrder(request.displayOrder());
        return experience;
    }

    public ExperienceResponse toResponse(Experience experience) {
        return new ExperienceResponse(
                experience.getId(),
                experience.getCompany(),
                experience.getCompanyUrl(),
                experience.getRole(),
                experience.getDescription(),
                experience.getLogoUrl(),
                experience.getStartDate(),
                experience.getEndDate(),
                experience.getLocation(),
                experience.getDisplayOrder()
        );
    }
}
