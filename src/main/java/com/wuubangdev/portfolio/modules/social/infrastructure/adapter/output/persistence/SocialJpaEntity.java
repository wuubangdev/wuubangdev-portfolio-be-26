package com.wuubangdev.portfolio.modules.social.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.infrastructure.persistence.base.BaseEntity;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "socials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SocialJpaEntity extends BaseEntity {
    private String facebook;
    private String github;
    private String linkedin;
    private String zalo;
    private String telegram;
    private String gmail;
    private String phone;
    private String address;
    private String addressGgMapLink;
}
