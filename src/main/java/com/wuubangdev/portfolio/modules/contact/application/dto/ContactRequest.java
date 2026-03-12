package com.wuubangdev.portfolio.modules.contact.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ContactRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        String subject,
        @NotBlank String message
) {}
