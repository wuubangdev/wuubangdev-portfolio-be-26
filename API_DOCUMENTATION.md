# API Documentation

Tài liệu này mô tả contract thực tế của backend hiện tại để frontend có thể gọi API trực tiếp.

## Base URL

```text
http://localhost:8080
```

## Auth Header

Với các endpoint admin hoặc endpoint yêu cầu đăng nhập:

```http
Authorization: Bearer <access_token>
```

## i18n và Multilingual Content

- Mọi endpoint đều có thể nhận `?lang=vi` hoặc `?lang=en`
- Với các entity đã hỗ trợ translation (`post`, `project`, `experience`, `education`, `profile`):
  - `locale`: locale backend đang trả về
  - `translated`: `true` nếu có bản dịch thật cho locale hiện tại
  - `translated=false` nếu backend đang fallback về nội dung gốc

Ví dụ:

```http
GET /api/v1/posts/hello-world?lang=en
GET /api/v1/projects/my-project?lang=vi
```

## Pagination Shape

Các endpoint phân trang trả về:

```json
{
  "content": [],
  "page": 0,
  "size": 10,
  "totalElements": 25,
  "totalPages": 3,
  "first": true,
  "last": false
}
```

## 1. Auth

### `POST /api/v1/auth/register`

Request:

```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123"
}
```

Response `201`:

```json
"Đăng ký người dùng thành công"
```

### `POST /api/v1/auth/login`

Request:

```json
{
  "username": "john_doe",
  "password": "password123"
}
```

Response `200`:

```json
{
  "accessToken": "jwt-access-token",
  "refreshToken": "jwt-refresh-token",
  "tokenType": "Bearer",
  "expiresIn": 86400000,
  "refreshExpiresIn": 604800000,
  "message": "Login successful"
}
```

### `POST /api/v1/auth/refresh`

Request:

```json
{
  "refreshToken": "jwt-refresh-token"
}
```

Response: cùng shape với `login`

### `POST /api/v1/auth/social/google`
### `POST /api/v1/auth/social/github`

Request:

```json
{
  "accessToken": "provider-access-token"
}
```

Response: cùng shape với `login`

### `GET /api/v1/auth/activate?token=...`

Response `200`:

```json
"Kích hoạt tài khoản thành công"
```

### `POST /api/v1/auth/forgot-password`

Request:

```json
{
  "email": "john@example.com"
}
```

Response `200`:

```json
"Đã gửi email đặt lại mật khẩu"
```

### `POST /api/v1/auth/reset-password`

Request:

```json
{
  "token": "reset-token",
  "newPassword": "newPassword123"
}
```

Response `200`:

```json
"Đặt lại mật khẩu thành công"
```

### `GET /api/v1/auth/me`

Response:

```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "roles": ["ROLE_USER"],
  "enabled": true,
  "userType": "BASIC"
}
```

`userType` có thể là:
- `BASIC`
- `GOOGLE`
- `GITHUB`

## 2. Profile

### `GET /api/v1/profile`

Response:

```json
{
  "id": 1,
  "fullName": "Wuu Bang Dev",
  "title": "Full Stack Developer",
  "bio": "Short bio",
  "avatarUrl": "https://...",
  "resumeUrl": "https://...",
  "location": "Da Nang",
  "email": "me@example.com",
  "phone": "0123456789",
  "socialLinks": [
    {
      "platform": "github",
      "url": "https://github.com/...",
      "icon": "github"
    }
  ],
  "locale": "en",
  "translated": true
}
```

### `PUT /api/v1/admin/profile`

Request:

```json
{
  "fullName": "Wuu Bang Dev",
  "title": "Full Stack Developer",
  "bio": "Vietnamese bio",
  "avatarUrl": "https://...",
  "resumeUrl": "https://...",
  "location": "Da Nang",
  "email": "me@example.com",
  "phone": "0123456789",
  "socialLinks": [
    {
      "platform": "github",
      "url": "https://github.com/...",
      "icon": "github"
    }
  ],
  "translations": [
    {
      "locale": "en",
      "fullName": "Wuu Bang Dev",
      "title": "Full Stack Developer",
      "bio": "English bio",
      "location": "Da Nang"
    }
  ]
}
```

### `PUT /api/v1/admin/profile/translations/{locale}`

Ví dụ `PUT /api/v1/admin/profile/translations/en`

Request:

```json
{
  "fullName": "Wuu Bang Dev",
  "title": "Full Stack Developer",
  "bio": "English bio",
  "location": "Da Nang"
}
```

## 3. Social

### `GET /api/v1/social`
### `PUT /api/v1/admin/social`

Request/response:

```json
{
  "id": 1,
  "facebook": "https://facebook.com/...",
  "github": "https://github.com/...",
  "linkedin": "https://linkedin.com/in/...",
  "zalo": "https://zalo.me/...",
  "telegram": "https://t.me/...",
  "gmail": "me@example.com",
  "phone": "0123456789",
  "address": "Da Nang, Vietnam",
  "addressGgMapLink": "https://maps.google.com/..."
}
```

Khi `PUT`, chỉ cần gửi cùng shape nhưng không cần `id`.

## 4. Setting

### `GET /api/v1/setting`
### `PUT /api/v1/admin/setting`

Request/response:

```json
{
  "id": 1,
  "logo": "https://...",
  "thumbnailImageSeo": "https://...",
  "titleSeo": "Portfolio SEO title",
  "descriptionSeo": "Portfolio SEO description"
}
```

## 5. Skills

### `GET /api/v1/skills`
### `POST /api/v1/admin/skills`
### `PUT /api/v1/admin/skills/{id}`
### `DELETE /api/v1/admin/skills/{id}`

Request:

```json
{
  "name": "Java",
  "category": "Backend",
  "level": 90,
  "icon": "devicon-java-plain",
  "displayOrder": 1,
  "isHidden": false
}
```

Response:

```json
{
  "id": 1,
  "name": "Java",
  "category": "Backend",
  "level": 90,
  "icon": "devicon-java-plain",
  "displayOrder": 1,
  "isHidden": false
}
```

## 6. Experiences

### `GET /api/v1/experiences`

Response item:

```json
{
  "id": 1,
  "company": "Cong ty A",
  "companyUrl": "https://company-a.com",
  "role": "Backend Developer",
  "description": "Mo ta",
  "logoUrl": "https://...",
  "startDate": "2023-01-01",
  "endDate": null,
  "location": "Da Nang",
  "displayOrder": 1,
  "isHidden": false,
  "locale": "en",
  "translated": true
}
```

### `POST /api/v1/admin/experiences`
### `PUT /api/v1/admin/experiences/{id}`

Request:

```json
{
  "company": "Cong ty A",
  "companyUrl": "https://company-a.com",
  "role": "Backend Developer",
  "description": "Noi dung goc",
  "logoUrl": "https://...",
  "startDate": "2023-01-01",
  "endDate": null,
  "location": "Da Nang",
  "displayOrder": 1,
  "isHidden": false,
  "translations": [
    {
      "locale": "en",
      "company": "Company A",
      "role": "Backend Developer",
      "description": "English content",
      "location": "Da Nang"
    }
  ]
}
```

### `PUT /api/v1/admin/experiences/{id}/translations/{locale}`

Request:

```json
{
  "company": "Company A",
  "role": "Backend Developer",
  "description": "English content",
  "location": "Da Nang"
}
```

## 7. Educations

Lưu ý: admin route của `education` hiện vẫn nằm trên prefix `/api/v1/educations/...`, không phải `/api/v1/admin/educations/...`.

### `GET /api/v1/educations`
### `GET /api/v1/educations/{id}`

Response item:

```json
{
  "id": 1,
  "institution": "FPT University",
  "degree": "Bachelor",
  "fieldOfStudy": "Software Engineering",
  "startDate": "2019-01-01",
  "endDate": "2023-01-01",
  "description": "Mo ta",
  "logoUrl": "https://...",
  "location": "Da Nang",
  "displayOrder": 1,
  "isPublic": true,
  "locale": "en",
  "translated": true
}
```

### `POST /api/v1/educations`
### `PUT /api/v1/educations/{id}`

Request:

```json
{
  "institution": "FPT University",
  "degree": "Bachelor",
  "fieldOfStudy": "Software Engineering",
  "startDate": "2019-01-01",
  "endDate": "2023-01-01",
  "description": "Noi dung goc",
  "logoUrl": "https://...",
  "location": "Da Nang",
  "displayOrder": 1,
  "isPublic": true,
  "translations": [
    {
      "locale": "en",
      "institution": "FPT University",
      "degree": "Bachelor",
      "fieldOfStudy": "Software Engineering",
      "description": "English content",
      "location": "Da Nang"
    }
  ]
}
```

### `PUT /api/v1/educations/{id}/translations/{locale}`

Request:

```json
{
  "institution": "FPT University",
  "degree": "Bachelor",
  "fieldOfStudy": "Software Engineering",
  "description": "English content",
  "location": "Da Nang"
}
```

### `PUT /api/v1/educations/{id}/order?order=1`
### `PUT /api/v1/educations/{id}/public?isPublic=true`

## 8. Languages

### `GET /api/v1/languages`
### `GET /api/v1/languages/{id}`
### `POST /api/v1/languages`
### `PUT /api/v1/languages/{id}`
### `DELETE /api/v1/languages/{id}`

Request/response item:

```json
{
  "id": 1,
  "code": "en",
  "name": "English",
  "isDefault": false
}
```

## 9. Categories

### `GET /api/v1/categories`
### `GET /api/v1/categories/{id}`
### `POST /api/v1/categories`
### `PUT /api/v1/categories/{id}`
### `DELETE /api/v1/categories/{id}`

Request/response item:

```json
{
  "id": 1,
  "name": "Technology",
  "slug": "technology",
  "description": "Technology posts"
}
```

## 10. Comments

### `GET /api/v1/comments/post/{postId}`
### `POST /api/v1/comments`
### `DELETE /api/v1/comments/{id}`

Request:

```json
{
  "postId": 1,
  "author": "Guest User",
  "content": "Great article",
  "parentId": null
}
```

Response:

```json
{
  "id": 1,
  "postId": 1,
  "author": "Guest User",
  "content": "Great article",
  "createdAt": "2026-04-17T15:00:00",
  "parentId": null
}
```

## 11. Posts

### `GET /api/v1/posts`
### `GET /api/v1/posts/paged?page=0&size=10`
### `GET /api/v1/posts/{slug}`
### `GET /api/v1/posts/recent?limit=5`
### `GET /api/v1/posts/{id}/related?limit=5`

Response item:

```json
{
  "id": 1,
  "title": "My First Post",
  "slug": "my-first-post",
  "category": "Technology",
  "content": "Post content",
  "summary": "Brief summary",
  "coverImageUrl": "https://...",
  "tags": ["spring", "java"],
  "published": true,
  "author": "Wuu Bang Dev",
  "titleSeo": "SEO title",
  "descriptionSeo": "SEO description",
  "thumbnailSeo": "https://...",
  "seoKeywords": ["spring", "java"],
  "canonicalUrl": "https://...",
  "indexable": true,
  "likes": 10,
  "hearts": 0,
  "commentsCount": 0,
  "shares": 2,
  "status": "PUBLISHED",
  "createdAt": "2026-04-17T15:00:00",
  "displayOrder": 1,
  "isHidden": false,
  "locale": "en",
  "translated": true
}
```

### `POST /api/v1/posts/{slug}/like`
### `DELETE /api/v1/posts/{slug}/like`
### `POST /api/v1/posts/{slug}/share`

Response:

```json
{
  "postId": 1,
  "slug": "my-first-post",
  "likes": 11,
  "shares": 2
}
```

### `POST /api/v1/admin/posts`
### `PUT /api/v1/admin/posts/{id}`

Request:

```json
{
  "title": "Bai viet goc",
  "slug": "bai-viet-goc",
  "category": "Technology",
  "content": "Noi dung goc",
  "summary": "Tom tat",
  "coverImageUrl": "https://...",
  "tags": ["spring", "java"],
  "published": true,
  "author": "Wuu Bang Dev",
  "titleSeo": "SEO title",
  "descriptionSeo": "SEO description",
  "thumbnailSeo": "https://...",
  "seoKeywords": ["spring", "java"],
  "canonicalUrl": "https://...",
  "indexable": true,
  "displayOrder": 1,
  "isHidden": false,
  "translations": [
    {
      "locale": "en",
      "title": "English title",
      "content": "English content",
      "summary": "English summary",
      "titleSeo": "English SEO title",
      "descriptionSeo": "English SEO description",
      "seoKeywords": ["spring", "java"]
    }
  ]
}
```

### `PUT /api/v1/admin/posts/{id}/translations/{locale}`

Request:

```json
{
  "title": "English title",
  "content": "English content",
  "summary": "English summary",
  "titleSeo": "English SEO title",
  "descriptionSeo": "English SEO description",
  "seoKeywords": ["spring", "java"]
}
```

### `PATCH /api/v1/admin/posts/{id}/status?status=PUBLISHED`
### `DELETE /api/v1/admin/posts/{id}`

## 12. Projects

### `GET /api/v1/projects`

Query params:
- `category`
- `featured`

### `GET /api/v1/projects/paged?page=0&size=10`

Query params:
- `category`
- `featured`

### `GET /api/v1/projects/featured`
### `GET /api/v1/projects/{slug}`
### `GET /api/v1/projects/{slug}/related?limit=3`

Response item:

```json
{
  "id": 1,
  "title": "Portfolio Backend",
  "slug": "portfolio-backend",
  "category": "Backend",
  "tags": ["spring", "mongodb"],
  "description": "Short description",
  "content": "Full content",
  "techStack": ["Java", "Spring Boot", "MongoDB"],
  "imageUrl": "https://...",
  "projectUrl": "https://...",
  "githubUrl": "https://github.com/...",
  "groupName": "Personal",
  "featured": true,
  "displayOrder": 1,
  "titleSeo": "SEO title",
  "descriptionSeo": "SEO description",
  "thumbnailSeo": "https://...",
  "seoKeywords": ["spring", "mongodb"],
  "canonicalUrl": "https://...",
  "indexable": true,
  "createdAt": "2026-04-17T15:00:00",
  "locale": "en",
  "translated": true
}
```

### `POST /api/v1/admin/projects`
### `PUT /api/v1/admin/projects/{id}`

Request:

```json
{
  "title": "Du an goc",
  "slug": "du-an-goc",
  "category": "Backend",
  "tags": ["spring", "mongodb"],
  "description": "Mo ta goc",
  "content": "Noi dung goc",
  "techStack": ["Java", "Spring Boot", "MongoDB"],
  "imageUrl": "https://...",
  "projectUrl": "https://...",
  "githubUrl": "https://github.com/...",
  "groupName": "Personal",
  "featured": true,
  "displayOrder": 1,
  "titleSeo": "SEO title",
  "descriptionSeo": "SEO description",
  "thumbnailSeo": "https://...",
  "seoKeywords": ["spring", "mongodb"],
  "canonicalUrl": "https://...",
  "indexable": true,
  "translations": [
    {
      "locale": "en",
      "title": "English project title",
      "description": "English description",
      "content": "English content",
      "titleSeo": "English SEO title",
      "descriptionSeo": "English SEO description",
      "seoKeywords": ["spring", "mongodb"]
    }
  ]
}
```

### `PUT /api/v1/admin/projects/{id}/translations/{locale}`

Request:

```json
{
  "title": "English project title",
  "description": "English description",
  "content": "English content",
  "titleSeo": "English SEO title",
  "descriptionSeo": "English SEO description",
  "seoKeywords": ["spring", "mongodb"]
}
```

### `DELETE /api/v1/admin/projects/{id}`

## 13. Contact

### `POST /api/v1/contact`

Request:

```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "subject": "Need collaboration",
  "message": "Hello, I want to contact you."
}
```

Response:

```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "subject": "Need collaboration",
  "message": "Hello, I want to contact you.",
  "read": false,
  "status": "NEW",
  "createdAt": "2026-04-17T15:00:00"
}
```

### Admin

- `GET /api/v1/admin/contacts`
- `PATCH /api/v1/admin/contacts/{id}/read`
- `PUT /api/v1/admin/contacts/{id}`
- `PATCH /api/v1/admin/contacts/{id}/status?status=PROCESSING`

## 14. Admin Users

### `GET /api/v1/admin/users`
### `GET /api/v1/admin/users/{id}`
### `PATCH /api/v1/admin/users/{id}/roles`

Update roles request:

```json
["ROLE_ADMIN", "ROLE_USER"]
```

Response:

```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "roles": ["ROLE_USER"],
  "enabled": true,
  "userType": "BASIC"
}
```

## FE Integration Notes

- Dùng `?lang=en` hoặc `?lang=vi` ở các màn public để lấy đúng bản dịch
- FE nên đọc:
  - `locale` để biết backend đang trả locale nào
  - `translated` để biết bản dịch có thật hay chỉ fallback
- Với admin CMS:
  - có thể lưu nội dung gốc bằng payload chính
  - có thể sửa từng locale bằng endpoint `PUT .../translations/{locale}`
- `education` và `category` hiện dùng route admin ngay trên prefix public nhưng vẫn được bảo vệ bằng `@PreAuthorize`
