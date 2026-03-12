package com.wuubangdev.portfolio.modules.profile.domain.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialLink {
    private String platform;
    private String url;
    private String icon;
}
