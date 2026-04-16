package com.wuubangdev.portfolio.modules.language.domain.model;

import lombok.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Language {
    private Long id;
    private String code; // e.g., "en", "vi"
    private String name; // e.g., "English", "Tiếng Việt"
    private Boolean isDefault;
}
