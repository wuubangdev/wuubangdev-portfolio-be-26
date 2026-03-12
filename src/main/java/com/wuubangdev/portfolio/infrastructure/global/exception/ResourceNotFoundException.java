package com.wuubangdev.portfolio.infrastructure.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, Object identifier) {
        super(resource + " not found with id: " + identifier);
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
