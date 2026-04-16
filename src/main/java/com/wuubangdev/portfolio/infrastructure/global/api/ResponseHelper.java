package com.wuubangdev.portfolio.infrastructure.global.api;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Locale;
import java.util.UUID;

/**
 * Response Helper for building standardized REST API responses
 * 
 * Usage:
 *   ResponseEntity<ApiResponse<PostResponse>> response = 
 *     ResponseHelper.ok("Post retrieved successfully", post);
 */
@Component
@RequiredArgsConstructor
public class ResponseHelper {

    private final MessageSource messageSource;

    /**
     * OK - 200
     */
    public <T> ResponseEntity<ApiResponse<T>> ok(String message, T data) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, message, data));
    }

    /**
     * OK - 200 without data
     */
    public <T> ResponseEntity<ApiResponse<T>> ok(String message) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, message));
    }

    /**
     * OK - 200 with key (localized)
     */
    public <T> ResponseEntity<ApiResponse<T>> ok(String messageKey, T data, String... params) {
        String message = getMessage(messageKey, params);
        return ok(message, data);
    }

    /**
     * CREATED - 201
     */
    public <T> ResponseEntity<ApiResponse<T>> created(String message, T data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(message, data));
    }

    /**
     * CREATED - 201 with key (localized)
     */
    public <T> ResponseEntity<ApiResponse<T>> created(String messageKey, T data, String... params) {
        String message = getMessage(messageKey, params);
        return created(message, data);
    }

    /**
     * UPDATED - 200
     */
    public <T> ResponseEntity<ApiResponse<T>> updated(String message, T data) {
        return ResponseEntity.ok(ApiResponse.updated(message, data));
    }

    /**
     * UPDATED - 200 with key (localized)
     */
    public <T> ResponseEntity<ApiResponse<T>> updated(String messageKey, T data, String... params) {
        String message = getMessage(messageKey, params);
        return updated(message, data);
    }

    /**
     * DELETED - 200
     */
    public <T> ResponseEntity<ApiResponse<T>> deleted(String message) {
        return ResponseEntity.ok(ApiResponse.deleted(message));
    }

    /**
     * DELETED - 200 with key (localized)
     */
    public <T> ResponseEntity<ApiResponse<T>> deleted(String messageKey, String... params) {
        String message = getMessage(messageKey, params);
        return deleted(message);
    }

    /**
     * NO CONTENT - 204
     */
    public <T> ResponseEntity<ApiResponse<T>> noContent() {
        return ResponseEntity.noContent().build();
    }

    /**
     * Paginated Response - 200
     */
    public <T> ResponseEntity<ApiResponse<T>> paginated(String message, PageResponse<T> pageResponse) {
        return ResponseEntity.ok(ApiResponse.paginated(message, pageResponse));
    }

    /**
     * Paginated Response - 200 with key (localized)
     */
    public <T> ResponseEntity<ApiResponse<T>> paginated(String messageKey, PageResponse<T> pageResponse, String... params) {
        String message = getMessage(messageKey, params);
        return paginated(message, pageResponse);
    }

    /**
     * BAD REQUEST - 400
     */
    public <T> ResponseEntity<ApiResponse<T>> badRequest(String message, String errorCode) {
        return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST, message, errorCode));
    }

    /**
     * BAD REQUEST - 400 with key (localized)
     */
    public <T> ResponseEntity<ApiResponse<T>> badRequest(String messageKey, String errorCode, String... params) {
        String message = getMessage(messageKey, params);
        return badRequest(message, errorCode);
    }

    /**
     * BAD REQUEST - 400 with details
     */
    public <T> ResponseEntity<ApiResponse<T>> badRequest(String message, String errorCode, Object details) {
        return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST, message, errorCode, details));
    }

    /**
     * UNAUTHORIZED - 401
     */
    public <T> ResponseEntity<ApiResponse<T>> unauthorized(String message) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error(HttpStatus.UNAUTHORIZED, message, "UNAUTHORIZED"));
    }

    /**
     * UNAUTHORIZED - 401 with key (localized)
     */
    public <T> ResponseEntity<ApiResponse<T>> unauthorized(String messageKey, String... params) {
        String message = getMessage(messageKey, params);
        return unauthorized(message);
    }

    /**
     * FORBIDDEN - 403
     */
    public <T> ResponseEntity<ApiResponse<T>> forbidden(String message) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error(HttpStatus.FORBIDDEN, message, "FORBIDDEN"));
    }

    /**
     * FORBIDDEN - 403 with key (localized)
     */
    public <T> ResponseEntity<ApiResponse<T>> forbidden(String messageKey, String... params) {
        String message = getMessage(messageKey, params);
        return forbidden(message);
    }

    /**
     * NOT FOUND - 404
     */
    public <T> ResponseEntity<ApiResponse<T>> notFound(String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(HttpStatus.NOT_FOUND, message, "NOT_FOUND"));
    }

    /**
     * NOT FOUND - 404 with key (localized)
     */
    public <T> ResponseEntity<ApiResponse<T>> notFound(String messageKey, String... params) {
        String message = getMessage(messageKey, params);
        return notFound(message);
    }

    /**
     * INTERNAL SERVER ERROR - 500
     */
    public <T> ResponseEntity<ApiResponse<T>> internalServerError(String message) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, message, "INTERNAL_SERVER_ERROR"));
    }

    /**
     * INTERNAL SERVER ERROR - 500 with key (localized)
     */
    public <T> ResponseEntity<ApiResponse<T>> internalServerError(String messageKey, String... params) {
        String message = getMessage(messageKey, params);
        return internalServerError(message);
    }

    /**
     * Custom HTTP Status
     */
    public <T> ResponseEntity<ApiResponse<T>> custom(HttpStatus status, String message, T data) {
        return ResponseEntity.status(status).body(ApiResponse.success(status, message, data));
    }

    /**
     * Get localized message
     */
    private String getMessage(String key, String... params) {
        try {
            Locale locale = getCurrentLocale();
            Object[] args = params != null && params.length > 0 ? params : null;
            return messageSource.getMessage(key, args, locale);
        } catch (Exception e) {
            return key; // Fallback to key if message not found
        }
    }

    /**
     * Get current locale from request context
     */
    private Locale getCurrentLocale() {
        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            Locale locale = (Locale) attrs.getRequest().getSession().getAttribute("org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE");
            return locale != null ? locale : Locale.getDefault();
        } catch (Exception e) {
            return Locale.getDefault();
        }
    }
}
