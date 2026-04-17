package com.wuubangdev.portfolio.modules.experience.domain.model;

import lombok.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class ExperienceTranslation {
    private Long id;
    private Long experienceId;
    private String locale;
    private String company;
    private String role;
    private String description;
    private String location;
}
