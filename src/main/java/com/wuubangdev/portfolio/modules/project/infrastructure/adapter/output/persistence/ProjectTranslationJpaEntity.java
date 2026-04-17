package com.wuubangdev.portfolio.modules.project.infrastructure.adapter.output.persistence;

import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "project_translations")
@CompoundIndex(name = "project_locale_unique", def = "{'projectId': 1, 'locale': 1}", unique = true)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ProjectTranslationJpaEntity {
    @org.springframework.data.annotation.Id
    private Long id;
    private Long projectId;
    private String locale;
    private String title;
    private String description;
    private String content;
    private String titleSeo;
    private String descriptionSeo;
    private List<String> seoKeywords;
}
