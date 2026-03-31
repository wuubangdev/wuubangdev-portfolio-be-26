package com.wuubangdev.portfolio.modules.experience.application.dto;

import java.time.LocalDate;

public record ExperienceResponse(
        Long id,
        String company,
        String companyUrl,
        String role,
        String description,
        String logoUrl,
        LocalDate startDate,
        LocalDate endDate,
        String location,
        Integer displayOrder
) {}
