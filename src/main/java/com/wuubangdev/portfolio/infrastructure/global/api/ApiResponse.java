package com.wuubangdev.portfolio.infrastructure.global.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * Generic REST API Response Entity
 * 
 * Standard RESTful response format for all API endpoints
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    /**
     * HTTP Status Code (200, 201, 400, 404, 500, etc.)
     */
    private Integer code;

    /**
     * Response status: "success", "error", "created", "updated", "deleted"
     */
    private String status;

    /**
     * Human-readable message (can be localized)
     */
    private String message;

    /**
     * Response data/content
     */
    private T data;

    /**
     * Timestamp when response was generated
     */
    private LocalDateTime timestamp;

    /**
     * Request ID for tracking (optional)
     */
    private String requestId;

    /**
     * Error details (for error responses)
     */
    private ErrorDetail error;

    /**
     * Pagination info (for paginated responses)
     */
    private PaginationInfo pagination;

    /**
     * Success response builder
     */
    public static <T> ApiResponse<T> success(HttpStatus status, String message, T data) {
        return ApiResponse.<T>builder()
                .code(status.value())
                .status("success")
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Success response without data
     */
    public static <T> ApiResponse<T> success(HttpStatus status, String message) {
        return success(status, message, null);
    }

    /**
     * Created response (201)
     */
    public static <T> ApiResponse<T> created(String message, T data) {
        return ApiResponse.<T>builder()
                .code(HttpStatus.CREATED.value())
                .status("created")
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Updated response
     */
    public static <T> ApiResponse<T> updated(String message, T data) {
        return ApiResponse.<T>builder()
                .code(HttpStatus.OK.value())
                .status("updated")
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Deleted response
     */
    public static <T> ApiResponse<T> deleted(String message) {
        return ApiResponse.<T>builder()
                .code(HttpStatus.OK.value())
                .status("deleted")
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Error response
     */
    public static <T> ApiResponse<T> error(HttpStatus status, String message, String errorCode) {
        return ApiResponse.<T>builder()
                .code(status.value())
                .status("error")
                .message(message)
                .error(ErrorDetail.builder()
                        .code(errorCode)
                        .message(message)
                        .build())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Error response with details
     */
    public static <T> ApiResponse<T> error(HttpStatus status, String message, String errorCode, Object details) {
        return ApiResponse.<T>builder()
                .code(status.value())
                .status("error")
                .message(message)
                .error(ErrorDetail.builder()
                        .code(errorCode)
                        .message(message)
                        .details(details)
                        .build())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Paginated response
     */
    public static <T> ApiResponse<T> paginated(String message, T data, int page, int size, long total) {
        return ApiResponse.<T>builder()
                .code(HttpStatus.OK.value())
                .status("success")
                .message(message)
                .data(data)
                .pagination(PaginationInfo.builder()
                        .page(page)
                        .size(size)
                        .total(total)
                        .totalPages((int) Math.ceil((double) total / size))
                        .hasNext(page < Math.ceil((double) total / size) - 1)
                        .hasPrevious(page > 0)
                        .build())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Paginated response from PageResponse
     */
    public static <T> ApiResponse<T> paginated(String message, PageResponse<T> pageResponse) {
        return ApiResponse.<T>builder()
                .code(HttpStatus.OK.value())
                .status("success")
                .message(message)
                .data((T) pageResponse.content())
                .pagination(PaginationInfo.builder()
                        .page(pageResponse.page())
                        .size(pageResponse.size())
                        .total(pageResponse.totalElements())
                        .totalPages(pageResponse.totalPages())
                        .hasNext(pageResponse.page() < pageResponse.totalPages() - 1)
                        .hasPrevious(pageResponse.page() > 0)
                        .build())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Check if response is successful
     */
    public boolean isSuccess() {
        return "success".equals(status) || "created".equals(status) || "updated".equals(status) || "deleted".equals(status);
    }

    /**
     * Check if response is error
     */
    public boolean isError() {
        return "error".equals(status);
    }

    /**
     * Error Detail DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ErrorDetail {
        private String code;
        private String message;
        private Object details;
    }

    /**
     * Pagination Info DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PaginationInfo {
        private Integer page;
        private Integer size;
        private Long total;
        private Integer totalPages;
        private Boolean hasNext;
        private Boolean hasPrevious;
    }
}
