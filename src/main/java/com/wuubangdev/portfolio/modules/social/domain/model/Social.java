package com.wuubangdev.portfolio.modules.social.domain.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Social {
    private Long id;
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
