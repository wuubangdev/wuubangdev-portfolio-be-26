package com.wuubangdev.portfolio.modules.setting.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.infrastructure.persistence.base.BaseEntity;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SettingJpaEntity extends BaseEntity {
    private String logo;
    private String thumbnailImageSeo;
    private String titleSeo;
    private String descriptionSeo;
}
