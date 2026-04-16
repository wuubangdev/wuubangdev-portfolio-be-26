package com.wuubangdev.portfolio.modules.contact.application.dto;

import java.time.LocalDateTime;

public record ContactResponse(Long id, String name, String email, String subject, String message, Boolean read, String status, LocalDateTime createdAt) {}
