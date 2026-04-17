package com.wuubangdev.portfolio.modules.experience.application.mapper;

import com.wuubangdev.portfolio.modules.experience.application.dto.ExperienceRequest;
import com.wuubangdev.portfolio.modules.experience.application.dto.ExperienceResponse;
import com.wuubangdev.portfolio.modules.experience.domain.model.Experience;
import com.wuubangdev.portfolio.modules.experience.domain.model.ExperienceTranslation;
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
                .isHidden(request.isHidden())
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
        experience.setIsHidden(request.isHidden());
        return experience;
    }

    public ExperienceResponse toResponse(Experience experience) {
        return toResponse(experience, null, false);
    }

    public ExperienceResponse toResponse(Experience experience, String locale) {
        return toResponse(experience, locale, false);
    }

    public ExperienceResponse toResponse(Experience experience, String locale, boolean translated) {
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
                experience.getDisplayOrder(),
                experience.getIsHidden(),
                locale,
                translated
        );
    }

    public Experience applyTranslation(Experience experience, ExperienceTranslation translation) {
        if (translation == null) {
            return experience;
        }

        return Experience.builder()
                .id(experience.getId())
                .company(defaultIfBlank(translation.getCompany(), experience.getCompany()))
                .companyUrl(experience.getCompanyUrl())
                .role(defaultIfBlank(translation.getRole(), experience.getRole()))
                .description(defaultIfBlank(translation.getDescription(), experience.getDescription()))
                .logoUrl(experience.getLogoUrl())
                .startDate(experience.getStartDate())
                .endDate(experience.getEndDate())
                .location(defaultIfBlank(translation.getLocation(), experience.getLocation()))
                .displayOrder(experience.getDisplayOrder())
                .isHidden(experience.getIsHidden())
                .build();
    }

    private String defaultIfBlank(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
