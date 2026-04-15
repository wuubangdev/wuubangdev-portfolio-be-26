package com.wuubangdev.portfolio.modules.profile.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.infrastructure.persistence.base.BaseEntity;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileJpaEntity extends BaseEntity {
    private String fullName;

    private String title;

    private String bio;

    private String avatarUrl;
    private String resumeUrl;
    private String location;
    private String email;
    private String phone;

    private List<SocialLinkEmbeddable> socialLinks;
}
