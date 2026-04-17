package com.wuubangdev.portfolio.modules.profile.domain.model;

import lombok.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class ProfileTranslation {
    private Long id;
    private Long profileId;
    private String locale;
    private String fullName;
    private String title;
    private String bio;
    private String location;
}
