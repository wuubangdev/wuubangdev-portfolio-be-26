package com.wuubangdev.portfolio.modules.profile.infrastructure.adapter.output.persistence;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialLinkEmbeddable {
    private String platform;
    private String url;
    private String icon;
}
