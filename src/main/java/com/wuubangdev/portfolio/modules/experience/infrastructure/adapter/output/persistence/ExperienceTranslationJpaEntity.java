package com.wuubangdev.portfolio.modules.experience.infrastructure.adapter.output.persistence;

import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "experience_translations")
@CompoundIndex(name = "experience_locale_unique", def = "{'experienceId': 1, 'locale': 1}", unique = true)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ExperienceTranslationJpaEntity {
    @org.springframework.data.annotation.Id
    private Long id;
    private Long experienceId;
    private String locale;
    private String company;
    private String role;
    private String description;
    private String location;
}
