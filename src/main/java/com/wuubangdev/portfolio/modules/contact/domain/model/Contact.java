package com.wuubangdev.portfolio.modules.contact.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Contact {
    private Long id;
    private String name;
    private String email;
    private String subject;
    private String message;
    private Boolean read;
    private String status; // PENDING, APPROVED, REJECTED
    private LocalDateTime createdAt;
}
