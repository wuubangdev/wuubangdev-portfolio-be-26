package com.wuubangdev.portfolio.modules.contact.domain.model;

import lombok.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Contact {
    private Long id;
    private String name;
    private String email;
    private String subject;
    private String message;
    private Boolean read;
}
