package com.wuubangdev.portfolio.modules.experience.domain.model;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Experience {
    private Long id;
    private String company;
    private String role;
    private String description;
    private String logoUrl;
    private LocalDate startDate;
    private LocalDate endDate; // null = hiện tại
    private String location;
    private Integer displayOrder;
}
