package com.wuubangdev.portfolio.modules.post.domain.model;

import lombok.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Category {
    private Long id;
    private String name;
    private String slug;
    private String description;
}
