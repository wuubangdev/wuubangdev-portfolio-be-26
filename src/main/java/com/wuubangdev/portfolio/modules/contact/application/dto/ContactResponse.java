package com.wuubangdev.portfolio.modules.contact.application.dto;

public record ContactResponse(Long id, String name, String email, String subject, String message, Boolean read) {}
