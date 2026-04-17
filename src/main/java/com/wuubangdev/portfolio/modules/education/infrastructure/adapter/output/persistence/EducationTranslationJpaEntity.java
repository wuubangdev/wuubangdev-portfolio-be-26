package com.wuubangdev.portfolio.modules.education.infrastructure.adapter.output.persistence;

import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "education_translations")
@CompoundIndex(name = "education_locale_unique", def = "{'educationId': 1, 'locale': 1}", unique = true)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class EducationTranslationJpaEntity {
    @org.springframework.data.annotation.Id
    private Long id;
    private Long educationId;
    private String locale;
    private String institution;
    private String degree;
    private String fieldOfStudy;
    private String description;
    private String location;
}
