package com.wuubangdev.portfolio.modules.skill.domain.model;

import lombok.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Skill {
    private Long id;
    private String name;
    private String category;
    private Integer level; // 1-100
    private String icon;
    private Integer displayOrder;
}
