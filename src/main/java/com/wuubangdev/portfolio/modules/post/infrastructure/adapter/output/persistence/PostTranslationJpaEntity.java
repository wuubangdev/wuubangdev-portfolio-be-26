package com.wuubangdev.portfolio.modules.post.infrastructure.adapter.output.persistence;

import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "post_translations")
@CompoundIndex(name = "post_locale_unique", def = "{'postId': 1, 'locale': 1}", unique = true)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PostTranslationJpaEntity {
    @org.springframework.data.annotation.Id
    private Long id;
    private Long postId;
    private String locale;
    private String title;
    private String content;
    private String summary;
    private String titleSeo;
    private String descriptionSeo;
    private List<String> seoKeywords;
}
