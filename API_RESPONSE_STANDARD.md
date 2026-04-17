# API Response Standard

Repo này hiện không dùng một wrapper JSON duy nhất cho mọi endpoint. Frontend nên đọc response theo 4 nhóm chính dưới đây.

## 1. Plain Object

Phần lớn endpoint trả thẳng object DTO.

Ví dụ:

```json
{
  "id": 1,
  "title": "Portfolio Backend",
  "slug": "portfolio-backend"
}
```

Áp dụng cho:
- `profile`
- `social`
- `setting`
- `skill`
- `experience`
- `education`
- `language`
- `category`
- `contact`
- `post`
- `project`
- `user profile`

## 2. Plain Array

Các endpoint list không phân trang trả thẳng mảng.

Ví dụ:

```json
[
  {
    "id": 1,
    "name": "Java"
  },
  {
    "id": 2,
    "name": "Spring Boot"
  }
]
```

## 3. Paginated Object

Các endpoint có `/paged` trả về `PageResponse<T>`:

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

Áp dụng cho:
- `GET /api/v1/posts/paged`
- `GET /api/v1/admin/posts/paged`
- `GET /api/v1/projects/paged`
- `GET /api/v1/admin/projects/paged`

## 4. Plain String

Một số endpoint auth trả string message.

Ví dụ:

```json
"Đăng ký người dùng thành công"
```

Áp dụng cho:
- `POST /api/v1/auth/register`
- `GET /api/v1/auth/activate`
- `POST /api/v1/auth/forgot-password`
- `POST /api/v1/auth/reset-password`

## Multilingual Entity Response

Các entity hỗ trợ translation có thêm 2 field chuẩn:

```json
{
  "locale": "en",
  "translated": true
}
```

Ý nghĩa:
- `locale`: locale backend đang resolve từ `?lang=...`
- `translated=true`: có bản dịch thật cho locale đó
- `translated=false`: backend đang fallback về dữ liệu gốc

Áp dụng cho:
- `PostResponse`
- `ProjectResponse`
- `ExperienceResponse`
- `EducationResponse`
- `ProfileResponse`

## Login Response

`POST /api/v1/auth/login`, `refresh`, `social/google`, `social/github` trả:

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

## Engagement Response

Các endpoint like/share post trả:

```json
{
  "postId": 1,
  "slug": "my-post",
  "likes": 10,
  "shares": 2
}
```

## Error Handling

Error hiện được Spring xử lý theo exception handler của project. FE nên xử lý tối thiểu theo HTTP status:

- `400`: dữ liệu request không hợp lệ
- `401`: chưa đăng nhập hoặc token sai
- `403`: không đủ quyền admin
- `404`: không tìm thấy resource
- `500`: lỗi server

## FE Recommendations

- Đừng giả định mọi response có `data`
- Kiểm tra kiểu response theo endpoint:
  - object
  - array
  - page object
  - string
- Với entity đa ngôn ngữ, luôn đọc thêm:
  - `locale`
  - `translated`
