package com.wuubangdev.portfolio.modules.education.application.dto;

import java.time.LocalDate;
import java.util.List;

public record EducationRequest(
        String institution,
        String degree,
        String fieldOfStudy,
        LocalDate startDate,
        LocalDate endDate,
        String description,
        String logoUrl,
        String location,
        Integer displayOrder,
        Boolean isPublic,
        List<EducationTranslationRequest> translations
) {
}
