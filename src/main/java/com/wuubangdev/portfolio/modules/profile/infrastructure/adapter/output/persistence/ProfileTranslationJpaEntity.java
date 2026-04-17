package com.wuubangdev.portfolio.modules.profile.infrastructure.adapter.output.persistence;

import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "profile_translations")
@CompoundIndex(name = "profile_locale_unique", def = "{'profileId': 1, 'locale': 1}", unique = true)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ProfileTranslationJpaEntity {
    @org.springframework.data.annotation.Id
    private Long id;
    private Long profileId;
    private String locale;
    private String fullName;
    private String title;
    private String bio;
    private String location;
}
