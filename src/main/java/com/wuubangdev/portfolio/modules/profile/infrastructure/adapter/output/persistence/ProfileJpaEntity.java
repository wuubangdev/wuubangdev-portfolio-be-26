package com.wuubangdev.portfolio.modules.profile.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.infrastructure.persistence.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private String avatarUrl;
    private String resumeUrl;
    private String location;
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "profile_social_links", joinColumns = @JoinColumn(name = "profile_id"))
    private List<SocialLinkEmbeddable> socialLinks;
}
