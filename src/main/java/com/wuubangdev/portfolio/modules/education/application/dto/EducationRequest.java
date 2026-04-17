package com.wuubangdev.portfolio.modules.education.application.dto;

import java.time.LocalDate;

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
        Boolean isPublic
) {
}
