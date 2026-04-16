# API Documentation - Wuubangdev Portfolio Backend

## Base URL
```
http://localhost:8080
```

## Language Support
All endpoints support internationalization. Switch language using query parameter:
```
?lang=en  # English
?lang=vi  # Vietnamese (default)
```

---

## 🔐 Authentication Endpoints

### 1. Register User
```
POST /api/v1/auth/register
```
**Request Body:**
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123"
}
```
**Response (201):**
```json
{
  "code": 201,
  "status": "created",
  "message": "Đăng ký người dùng thành công",
  "timestamp": "2024-04-16T10:30:00"
}
```
**i18n:** Supports Vietnamese and English messages. Add `?lang=en` for English response.

---

### 2. Login
```
POST /api/v1/auth/login
```
**Request Body:**
```json
{
  "username": "john_doe",
  "password": "password123"
}
```
**Response (200):**
```json
{
  "code": 200,
  "status": "success",
  "message": "Đăng nhập thành công",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "message": "Login successful"
  },
  "timestamp": "2024-04-16T10:30:00"
}
```

**Headers for subsequent requests:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

### 3. Get Current User Profile
```
GET /api/v1/auth/me
```
**Headers:**
```
Authorization: Bearer {token}
```
**Response (200):**
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "roles": ["ROLE_USER", "ROLE_ADMIN"]
}
```

---

## 👥 User Management Endpoints (Admin Only)

### 1. Get All Users
```
GET /api/v1/admin/users
```
**Headers:**
```
Authorization: Bearer {admin_token}
```
**Response (200):**
```json
{
  "code": 200,
  "status": "success",
  "message": "Users retrieved successfully",
  "data": [
    {
      "username": "john_doe",
      "email": "john@example.com",
      "roles": ["ROLE_USER"]
    }
  ],
  "timestamp": "2024-04-16T10:30:00"
}
```

---

### 2. Get User by ID
```
GET /api/v1/admin/users/{id}
```
**Parameters:**
- `id` (required): User ID

**Response (200):**
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "roles": ["ROLE_USER"]
}
```

---

### 3. Update User Roles
```
PATCH /api/v1/admin/users/{id}/roles
```
**Headers:**
```
Authorization: Bearer {admin_token}
Content-Type: application/json
```
**Request Body:**
```json
["ROLE_ADMIN", "ROLE_MODERATOR"]
```
**Response (200):**
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "roles": ["ROLE_ADMIN", "ROLE_MODERATOR"]
}
```

---

## 📝 Post/Blog Endpoints

### 1. Get Published Posts
```
GET /api/v1/posts
```
**Response (200):**
```json
[
  {
    "id": 1,
    "title": "My First Post",
    "slug": "my-first-post",
    "category": "Technology",
    "content": "# Content here...",
    "summary": "Brief summary",
    "coverImageUrl": "https://example.com/image.jpg",
    "tags": ["javascript", "react"],
    "published": true,
    "author": "John Doe",
    "titleSeo": "SEO Title",
    "descriptionSeo": "SEO Description",
    "likes": 10,
    "hearts": 5,
    "commentsCount": 2,
    "shares": 1,
    "status": "APPROVED",
    "createdAt": "2024-04-16T10:00:00",
    "displayOrder": 1,
    "isHidden": false
  }
]
```

---

### 2. Get Published Posts (Paginated)
```
GET /api/v1/posts/paged?page=0&size=10
```
**Parameters:**
- `page` (optional, default: 0): Page number
- `size` (optional, default: 10): Page size

**Response (200):**
```json
{
  "code": 200,
  "status": "success",
  "message": "Posts retrieved successfully",
  "data": [
    {
      "id": 1,
      "title": "My First Post",
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

---

### 3. Get Post by Slug
```
GET /api/v1/posts/{slug}
```
**Parameters:**
- `slug` (required): Post slug

**Response (200):**
```json
{
  "id": 1,
  "title": "My First Post",
  ...
}
```

---

### 4. Get Recent Posts
```
GET /api/v1/posts/recent?limit=5
```
**Parameters:**
- `limit` (optional, default: 5): Number of recent posts

---

### 5. Get Related Posts
```
GET /api/v1/posts/{id}/related?limit=5
```
**Parameters:**
- `id` (required): Post ID
- `limit` (optional, default: 5): Number of related posts

---

### 6. Get All Posts (Admin)
```
GET /api/v1/admin/posts
```
**Headers:**
```
Authorization: Bearer {admin_token}
```

---

### 7. Create Post (Admin)
```
POST /api/v1/admin/posts
```
**Headers:**
```
Authorization: Bearer {admin_token}
Content-Type: application/json
```
**Request Body:**
```json
{
  "title": "New Post",
  "slug": "new-post",
  "category": "Technology",
  "content": "# Post content...",
  "summary": "Brief summary",
  "coverImageUrl": "https://example.com/image.jpg",
  "tags": ["javascript"],
  "published": true,
  "author": "John Doe",
  "titleSeo": "SEO Title",
  "descriptionSeo": "SEO Description",
  "thumbnailSeo": "https://example.com/thumb.jpg",
  "displayOrder": 1,
  "isHidden": false
}
```
**Response (201):** Full post object

---

### 8. Update Post (Admin)
```
PUT /api/v1/admin/posts/{id}
```
**Headers:**
```
Authorization: Bearer {admin_token}
Content-Type: application/json
```
**Request Body:** Same as create post

---

### 9. Delete Post (Admin)
```
DELETE /api/v1/admin/posts/{id}
```
**Headers:**
```
Authorization: Bearer {admin_token}
```
**Response (204):** No content

---

### 10. Change Post Status (Admin) ⭐ NEW
```
PATCH /api/v1/admin/posts/{id}/status?status=APPROVED
```
**Headers:**
```
Authorization: Bearer {admin_token}
```
**Parameters:**
- `id` (required): Post ID
- `status` (required): New status (PENDING, APPROVED, REJECTED)

**Response (200):** Updated post object

---

## 🏆 Skill Endpoints

### 1. Get All Skills
```
GET /api/v1/skills
```
**Response (200):**
```json
[
  {
    "id": 1,
    "name": "JavaScript",
    "category": "Programming",
    "level": 90,
    "icon": "js-icon.png",
    "displayOrder": 1,
    "isHidden": false
  }
]
```

---

### 2. Create Skill (Admin)
```
POST /api/v1/admin/skills
```
**Headers:**
```
Authorization: Bearer {admin_token}
Content-Type: application/json
```
**Request Body:**
```json
{
  "name": "JavaScript",
  "category": "Programming",
  "level": 90,
  "icon": "js-icon.png",
  "displayOrder": 1,
  "isHidden": false
}
```

---

### 3. Update Skill (Admin)
```
PUT /api/v1/admin/skills/{id}
```

---

### 4. Delete Skill (Admin)
```
DELETE /api/v1/admin/skills/{id}
```

---

## 💼 Experience Endpoints

### 1. Get All Experiences
```
GET /api/v1/experiences
```
**Response (200):**
```json
[
  {
    "id": 1,
    "company": "Tech Corp",
    "companyUrl": "https://techcorp.com",
    "role": "Senior Developer",
    "description": "Working on amazing projects",
    "logoUrl": "https://example.com/logo.jpg",
    "startDate": "2020-01-15",
    "endDate": null,
    "location": "Ho Chi Minh City",
    "displayOrder": 1,
    "isHidden": false
  }
]
```

---

### 2. Create Experience (Admin)
```
POST /api/v1/admin/experiences
```
**Request Body:**
```json
{
  "company": "Tech Corp",
  "companyUrl": "https://techcorp.com",
  "role": "Senior Developer",
  "description": "Working on amazing projects",
  "logoUrl": "https://example.com/logo.jpg",
  "startDate": "2020-01-15",
  "endDate": null,
  "location": "Ho Chi Minh City",
  "displayOrder": 1,
  "isHidden": false
}
```

---

### 3. Update Experience (Admin)
```
PUT /api/v1/admin/experiences/{id}
```

---

### 4. Delete Experience (Admin)
```
DELETE /api/v1/admin/experiences/{id}
```

---

## 📚 Education Endpoints

### 1. Get All Educations
```
GET /api/v1/educations
```

---

### 2. Get Education by ID
```
GET /api/v1/educations/{id}
```

---

### 3. Create Education (Admin)
```
POST /api/v1/educations
```
**Request Body:**
```json
{
  "institution": "University of Technology",
  "degree": "Bachelor",
  "fieldOfStudy": "Computer Science",
  "startDate": "2018-09-01",
  "endDate": "2022-06-01",
  "description": "Great experience",
  "location": "Ho Chi Minh City",
  "displayOrder": 1,
  "isPublic": true
}
```

---

### 4. Update Education (Admin)
```
PUT /api/v1/educations/{id}
```

---

### 5. Delete Education (Admin)
```
DELETE /api/v1/educations/{id}
```

---

### 6. Set Education Display Order (Admin)
```
PUT /api/v1/educations/{id}/order?order=1
```

---

### 7. Toggle Education Visibility (Admin)
```
PUT /api/v1/educations/{id}/public?isPublic=true
```

---

## 🚀 Project Endpoints

### 1. Get All Projects
```
GET /api/v1/projects
```
**Response (200):**
```json
[
  {
    "id": 1,
    "title": "Amazing Project",
    "slug": "amazing-project",
    "category": "Web Development",
    "tags": ["react", "node.js"],
    "description": "Project description",
    "content": "Detailed content",
    "techStack": ["React", "Node.js", "MongoDB"],
    "imageUrl": "https://example.com/image.jpg",
    "projectUrl": "https://project.com",
    "githubUrl": "https://github.com/...",
    "groupName": "group1",
    "featured": true,
    "displayOrder": 1
  }
]
```

---

### 2. Get Project by Slug
```
GET /api/v1/projects/{slug}
```

---

### 3. Create Project (Admin)
```
POST /api/v1/admin/projects
```
**Request Body:**
```json
{
  "title": "Amazing Project",
  "slug": "amazing-project",
  "category": "Web Development",
  "tags": ["react", "node.js"],
  "description": "Project description",
  "content": "Detailed content",
  "techStack": ["React", "Node.js", "MongoDB"],
  "imageUrl": "https://example.com/image.jpg",
  "projectUrl": "https://project.com",
  "githubUrl": "https://github.com/...",
  "groupName": "group1",
  "featured": true,
  "displayOrder": 1
}
```

---

### 4. Update Project (Admin)
```
PUT /api/v1/admin/projects/{id}
```

---

### 5. Delete Project (Admin)
```
DELETE /api/v1/admin/projects/{id}
```

---

## 📞 Contact Endpoints

### 1. Submit Contact Form
```
POST /api/v1/contact
```
**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "subject": "Inquiry",
  "message": "Hello, I have a question..."
}
```
**Response (201):**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "subject": "Inquiry",
  "message": "Hello, I have a question...",
  "read": false,
  "status": "PENDING",
  "createdAt": "2024-04-16T10:00:00"
}
```

---

### 2. Get All Contacts (Admin)
```
GET /api/v1/admin/contacts
```

---

### 3. Mark Contact as Read (Admin)
```
PATCH /api/v1/admin/contacts/{id}/read
```

---

### 4. Update Contact (Admin)
```
PUT /api/v1/admin/contacts/{id}
```

---

### 5. Change Contact Status (Admin)
```
PATCH /api/v1/admin/contacts/{id}/status?status=APPROVED
```
**Parameters:**
- `status`: PENDING, APPROVED, REJECTED

---

## 🌐 Language Endpoints

### 1. Get All Languages
```
GET /api/v1/languages
```

---

### 2. Get Language by ID
```
GET /api/v1/languages/{id}
```

---

### 3. Create Language (Admin)
```
POST /api/v1/languages
```
**Request Body:**
```json
{
  "name": "English",
  "code": "en",
  "level": 90,
  "displayOrder": 1
}
```

---

### 4. Update Language (Admin)
```
PUT /api/v1/languages/{id}
```

---

### 5. Delete Language (Admin)
```
DELETE /api/v1/languages/{id}
```

---

## 🏷️ Category Endpoints (Post Categories)

### 1. Get All Categories
```
GET /api/v1/post/categories
```

---

### 2. Create Category (Admin)
```
POST /api/v1/admin/post/categories
```
**Request Body:**
```json
{
  "name": "Technology",
  "slug": "technology",
  "description": "Posts about technology"
}
```

---

## 💬 Comment Endpoints

### 1. Add Comment
```
POST /api/v1/posts/{postId}/comments
```
**Request Body:**
```json
{
  "content": "Great post!",
  "authorName": "John Doe"
}
```

---

## ⚠️ Error Responses

All error responses now follow the standardized ApiResponse format:

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

### Standardized Response Format
Every API response includes:
- **code**: HTTP status code
- **status**: Response status (success, created, updated, deleted, error)
- **message**: Human-readable message (localized)
- **data**: Response data (for successful responses)
- **error**: Error details (for error responses)
- **pagination**: Pagination info (for paginated responses)
- **timestamp**: Response generation timestamp

### Common HTTP Status Codes:
- `200 OK` - Success
- `201 Created` - Resource created
- `204 No Content` - Success (no body)
- `400 Bad Request` - Invalid input
- `401 Unauthorized` - Missing/invalid token
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

---

## 🔒 Security

### Authentication
- Use JWT token from login endpoint
- Include in Authorization header: `Authorization: Bearer {token}`
- Token expires in 24 hours (configurable)

### Authorization
- Roles: `ROLE_USER`, `ROLE_ADMIN`, `ROLE_MODERATOR`
- Admin endpoints require `ROLE_ADMIN`
- Check `@PreAuthorize("hasRole('ADMIN')")` annotations

---

## 🌍 Internationalization

All responses support multiple languages. Switch using `?lang` parameter:

```
GET /api/v1/posts?lang=en      # English
GET /api/v1/posts?lang=vi      # Vietnamese
```

### Supported Languages:
- `en` - English
- `vi` - Vietnamese (default)

Error messages, validation messages, and success messages are automatically translated.

---

## 📋 Request/Response Examples

### Example: Create and Publish a Post
```
1. POST /api/v1/admin/posts
   {
     "title": "New Article",
     "slug": "new-article",
     ...
   }
   → Returns post with status: "PENDING"

2. PATCH /api/v1/admin/posts/{id}/status?status=APPROVED
   → Changes status to "APPROVED"
   → Post becomes visible to public via GET /api/v1/posts
```

---

## 🔄 Pagination

Endpoints supporting pagination now return responses with pagination metadata:

**Request:**
```
GET /api/v1/posts/paged?page=0&size=10
```

**Response (200):**
```json
{
  "code": 200,
  "status": "success",
  "message": "Posts retrieved successfully",
  "data": [
    { "id": 1, "title": "Post 1", ... },
    ...
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

**Pagination Fields:**
- `page`: Current page number (0-indexed)
- `size`: Items per page
- `total`: Total number of items
- `totalPages`: Total number of pages
- `hasNext`: Whether there's a next page
- `hasPrevious`: Whether there's a previous page

---

## 📞 Support

For API response format details, refer to **API_RESPONSE_STANDARD.md**

For i18n configuration, refer to **I18N_README.md**

For issues or questions, please contact the development team.

**Backend Repository**: https://github.com/wuubangdev/wuubangdev-portfolio-be-26

---

## 📚 Documentation Files

- **API_DOCUMENTATION.md** - Complete API endpoint reference (this file)
- **API_RESPONSE_STANDARD.md** - Response format, ResponseHelper usage, examples
- **I18N_README.md** - Internationalization setup and usage
- **I18N_README.md** - Internationalization configuration

