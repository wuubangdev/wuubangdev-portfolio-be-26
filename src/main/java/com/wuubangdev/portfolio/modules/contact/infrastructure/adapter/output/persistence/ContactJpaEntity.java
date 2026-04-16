package com.wuubangdev.portfolio.modules.contact.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.infrastructure.persistence.base.BaseEntity;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "contacts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ContactJpaEntity extends BaseEntity {
    private String name;

    private String email;

    private String subject;

    private String message;

    private Boolean read;

    private String status;

    private LocalDateTime createdAt;
}
