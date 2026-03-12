package com.wuubangdev.portfolio.modules.profile.domain.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    private Long id;
    private String fullName;
    private String title;
    private String bio;
    private String avatarUrl;
    private String resumeUrl;
    private String location;
    private String email;
    private List<SocialLink> socialLinks;
}
