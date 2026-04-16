# API Response Standard Documentation

## Overview
Standardized REST API Response Entity that includes status code, message, data, timestamp, and follows RESTful API best practices.

## Response Structure

### Success Response Format
```json
{
  "code": 200,
  "status": "success",
  "message": "Post retrieved successfully",
  "data": {
    "id": 1,
    "title": "My Post",
    ...
  },
  "timestamp": "2024-04-16T10:30:00"
}
```

### Error Response Format
```json
{
  "code": 404,
  "status": "error",
  "message": "Post not found",
  "error": {
    "code": "NOT_FOUND",
    "message": "Post not found",
    "details": null
  },
  "timestamp": "2024-04-16T10:30:00"
}
```

### Paginated Response Format
```json
{
  "code": 200,
  "status": "success",
  "message": "Posts retrieved successfully",
  "data": [
    {
      "id": 1,
      "title": "Post 1",
      ...
    }
  ],
  "pagination": {
    "page": 0,
    "size": 10,
    "total": 25,
    "totalPages": 3,
    "hasNext": true,
    "hasPrevious": false
  },
  "timestamp": "2024-04-16T10:30:00"
}
```

## Response Status Types

| Status | HTTP Code | Usage |
|--------|-----------|-------|
| `success` | 200 | Successful GET/PUT operations |
| `created` | 201 | Successful POST operations |
| `updated` | 200 | Successful UPDATE operations |
| `deleted` | 200 | Successful DELETE operations |
| `error` | Various | Any error condition |

## HTTP Status Codes

| Code | Meaning | Example |
|------|---------|---------|
| 200 | OK | Data retrieved/updated successfully |
| 201 | Created | New resource created |
| 204 | No Content | Delete successful (no body) |
| 400 | Bad Request | Invalid input/validation error |
| 401 | Unauthorized | Missing/invalid authentication token |
| 403 | Forbidden | Insufficient permissions |
| 404 | Not Found | Resource not found |
| 500 | Internal Server Error | Server error |

## Usage Examples

### In Controllers

```java
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final ResponseHelper responseHelper;

    // Success Response - 200
    @GetMapping("/api/v1/posts/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> getPostById(@PathVariable Long id) {
        PostResponse post = postService.getById(id);
        return responseHelper.ok("Post retrieved successfully", post);
    }

    // Created Response - 201
    @PostMapping("/api/v1/admin/posts")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PostResponse>> create(@Valid @RequestBody PostRequest request) {
        PostResponse post = postService.create(request);
        return responseHelper.created("post.created", post);
    }

    // Updated Response - 200
    @PutMapping("/api/v1/admin/posts/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PostResponse>> update(@PathVariable Long id, @Valid @RequestBody PostRequest request) {
        PostResponse post = postService.update(id, request);
        return responseHelper.updated("post.updated", post);
    }

    // Deleted Response - 200
    @DeleteMapping("/api/v1/admin/posts/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        postService.delete(id);
        return responseHelper.deleted("post.deleted");
    }

    // Paginated Response - 200
    @GetMapping("/api/v1/posts/paged")
    public ResponseEntity<ApiResponse<List<PostResponse>>> getPostsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<PostResponse> pageResponse = postService.getPublishedPostsPaged(page, size);
        return responseHelper.paginated("Posts retrieved successfully", pageResponse);
    }

    // Error Response - 404
    @GetMapping("/api/v1/posts/invalid")
    public ResponseEntity<ApiResponse<Void>> invalid() {
        return responseHelper.notFound("post.not.found");
    }
}
```

### Response Methods Available

#### Success Responses
```java
responseHelper.ok(message, data);                      // 200 OK
responseHelper.ok(messageKey, data, params);           // 200 OK with localization
responseHelper.created(message, data);                 // 201 Created
responseHelper.created(messageKey, data, params);      // 201 Created with localization
responseHelper.updated(message, data);                 // 200 Updated
responseHelper.updated(messageKey, data, params);      // 200 Updated with localization
responseHelper.deleted(message);                       // 200 Deleted
responseHelper.deleted(messageKey, params);            // 200 Deleted with localization
responseHelper.paginated(message, pageResponse);       // 200 Paginated
responseHelper.paginated(messageKey, pageResponse);    // 200 Paginated with localization
```

#### Error Responses
```java
responseHelper.badRequest(message, errorCode);                    // 400 Bad Request
responseHelper.badRequest(messageKey, errorCode, params);         // 400 with localization
responseHelper.badRequest(message, errorCode, details);           // 400 with details
responseHelper.unauthorized(message);                             // 401 Unauthorized
responseHelper.unauthorized(messageKey, params);                  // 401 with localization
responseHelper.forbidden(message);                                // 403 Forbidden
responseHelper.forbidden(messageKey, params);                     // 403 with localization
responseHelper.notFound(message);                                 // 404 Not Found
responseHelper.notFound(messageKey, params);                      // 404 with localization
responseHelper.internalServerError(message);                      // 500 Server Error
responseHelper.internalServerError(messageKey, params);           // 500 with localization
```

## Response Components

### ApiResponse<T>
Generic response wrapper with following fields:

| Field | Type | Description |
|-------|------|-------------|
| `code` | Integer | HTTP status code (200, 201, 400, 404, etc.) |
| `status` | String | Response status ("success", "error", "created", "updated", "deleted") |
| `message` | String | Human-readable message (localized) |
| `data` | T | Response data/content (generic type) |
| `timestamp` | LocalDateTime | When response was generated |
| `requestId` | String | Optional request tracking ID |
| `error` | ErrorDetail | Error details (for error responses) |
| `pagination` | PaginationInfo | Pagination info (for paginated responses) |

### ErrorDetail
Error information object:

```json
{
  "code": "NOT_FOUND",
  "message": "Post not found",
  "details": null
}
```

### PaginationInfo
Pagination metadata:

```json
{
  "page": 0,
  "size": 10,
  "total": 25,
  "totalPages": 3,
  "hasNext": true,
  "hasPrevious": false
}
```

## Localization Support

All responses support multi-language messages via i18n:

```java
// Vietnamese message
responseHelper.ok("post.created", post);  // ?lang=vi

// English message
responseHelper.ok("post.created", post);  // ?lang=en
```

Message keys are defined in:
- `messages.properties` (default)
- `messages_vi.properties` (Vietnamese)
- `messages_en.properties` (English)

## Example API Workflows

### Create and Retrieve Post

```
1. POST /api/v1/admin/posts
   Request: { title, slug, ... }
   Response (201):
   {
     "code": 201,
     "status": "created",
     "message": "Post created successfully",
     "data": { id: 1, title, ... },
     "timestamp": "2024-04-16T10:30:00"
   }

2. GET /api/v1/posts/1
   Response (200):
   {
     "code": 200,
     "status": "success",
     "message": "Post retrieved successfully",
     "data": { id: 1, title, ... },
     "timestamp": "2024-04-16T10:35:00"
   }
```

### Handle Errors

```
1. GET /api/v1/posts/999 (non-existent)
   Response (404):
   {
     "code": 404,
     "status": "error",
     "message": "Post not found",
     "error": {
       "code": "NOT_FOUND",
       "message": "Post not found"
     },
     "timestamp": "2024-04-16T10:40:00"
   }

2. POST /api/v1/admin/posts (without admin token)
   Response (403):
   {
     "code": 403,
     "status": "error",
     "message": "Access denied",
     "error": {
       "code": "FORBIDDEN",
       "message": "Access denied"
     },
     "timestamp": "2024-04-16T10:45:00"
   }
```

## Best Practices

1. **Always use ResponseHelper** - Ensures consistency across all endpoints
2. **Use message keys** - Enable i18n support via localization files
3. **Include timestamp** - Track when response was generated
4. **Use appropriate HTTP codes** - Follow REST standards
5. **Provide meaningful messages** - Help clients understand responses
6. **Paginate large datasets** - Use PaginationInfo for list responses
7. **Include error details** - When applicable, add error codes and details

## Migration Guide

### Old Response (without wrapper)
```java
return ResponseEntity.ok(post);
```

### New Response (with ApiResponse)
```java
return responseHelper.ok("Post retrieved successfully", post);
```

This provides:
- ✅ Consistent response format
- ✅ Status code visibility
- ✅ Localized messages
- ✅ Timestamp tracking
- ✅ Error handling
- ✅ Pagination support
- ✅ RESTful compliance
