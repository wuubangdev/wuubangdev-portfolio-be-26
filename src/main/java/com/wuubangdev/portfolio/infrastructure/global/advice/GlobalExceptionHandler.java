package com.wuubangdev.portfolio.infrastructure.global.advice;

import com.wuubangdev.portfolio.infrastructure.global.exception.BusinessException;
import com.wuubangdev.portfolio.infrastructure.global.exception.ResourceNotFoundException;
import com.wuubangdev.portfolio.infrastructure.global.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    // 1. Lỗi validation (400) — @Valid thất bại
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        Locale locale = getLocaleFromRequest(request);
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fe -> fe.getDefaultMessage() != null ? fe.getDefaultMessage() : getMessage("validation.invalid", locale),
                        (a, b) -> a
                ));
        Map<String, Object> body = buildBody(HttpStatus.BAD_REQUEST, getMessage("error.bad.request", locale), locale);
        body.put("errors", errors);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // 2. Lỗi nghiệp vụ (400)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(BusinessException ex, WebRequest request) {
        Locale locale = getLocaleFromRequest(request);
        return new ResponseEntity<>(buildBody(HttpStatus.BAD_REQUEST, ex.getMessage(), locale), HttpStatus.BAD_REQUEST);
    }

    // 3. Không tìm thấy tài nguyên (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        Locale locale = getLocaleFromRequest(request);
        return new ResponseEntity<>(buildBody(HttpStatus.NOT_FOUND, ex.getMessage(), locale), HttpStatus.NOT_FOUND);
    }

    // 4. Chưa xác thực (401)
    @ExceptionHandler({UnauthorizedException.class, AuthenticationException.class})
    public ResponseEntity<Object> handleUnauthorizedException(RuntimeException ex, WebRequest request) {
        Locale locale = getLocaleFromRequest(request);
        return new ResponseEntity<>(buildBody(HttpStatus.UNAUTHORIZED, getMessage("error.unauthorized", locale), locale), HttpStatus.UNAUTHORIZED);
    }

    // 5. Không có quyền (403)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        Locale locale = getLocaleFromRequest(request);
        return new ResponseEntity<>(buildBody(HttpStatus.FORBIDDEN, getMessage("auth.access.denied", locale), locale), HttpStatus.FORBIDDEN);
    }

    // 6. Fallback (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
        Locale locale = getLocaleFromRequest(request);
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(buildBody(HttpStatus.INTERNAL_SERVER_ERROR, getMessage("error.internal", locale), locale), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String, Object> buildBody(HttpStatus status, String message, Locale locale) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return body;
    }

    private String getMessage(String key, Locale locale) {
        try {
            return messageSource.getMessage(key, null, locale);
        } catch (Exception e) {
            return key; // Fallback to key if message not found
        }
    }

    private Locale getLocaleFromRequest(WebRequest request) {
        // Try to get locale from request attributes or default to system locale
        try {
            return (Locale) request.getAttribute("org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE", WebRequest.SCOPE_SESSION);
        } catch (Exception e) {
            return Locale.getDefault();
        }
    }
}