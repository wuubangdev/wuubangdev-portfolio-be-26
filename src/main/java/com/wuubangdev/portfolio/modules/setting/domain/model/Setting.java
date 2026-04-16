package com.wuubangdev.portfolio.modules.setting.domain.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Setting {
    private Long id;
    private String logo;
    private String thumbnailImageSeo;
    private String titleSeo;
    private String descriptionSeo;
}
