package com.wuubangdev.portfolio.modules.post.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.infrastructure.persistence.base.BaseEntity;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "posts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PostJpaEntity extends BaseEntity {
    private String title;

    @Indexed(unique = true)
    private String slug;

    private String category;

    private String content;

    private String summary;

    private String coverImageUrl;

    private List<String> tags;

    private Boolean published;
}
