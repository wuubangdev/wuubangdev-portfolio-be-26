package com.wuubangdev.portfolio.modules.education.domain.model;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Education {
    private Long id;
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
