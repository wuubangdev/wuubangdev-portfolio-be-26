package com.wuubangdev.portfolio.modules.education.domain.model;

import lombok.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class EducationTranslation {
    private Long id;
    private Long educationId;
    private String locale;
    private String institution;
    private String degree;
    private String fieldOfStudy;
    private String description;
    private String location;
}
