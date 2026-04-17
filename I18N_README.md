# I18N Guide

Repo hiện hỗ trợ i18n cho:

- message text của auth/contact và một số business message
- dữ liệu content của entity qua `translation document`

## 1. Message i18n

Các file message:

- `src/main/resources/messages.properties`
- `src/main/resources/messages_vi.properties`
- `src/main/resources/messages_en.properties`

Locale mặc định:

```text
vi
```

Chuyển locale bằng query param:

```http
GET /api/v1/profile?lang=en
GET /api/v1/posts/my-post?lang=vi
```

## 2. Entity Content i18n

Hiện đã hỗ trợ cho:

- `post`
- `project`
- `experience`
- `education`
- `profile`

Mô hình dùng:

- 1 document chính chứa dữ liệu gốc
- 1 collection translation riêng cho từng entity
- khóa duy nhất theo `entityId + locale`

Ví dụ:

- `post_translations`
- `project_translations`
- `experience_translations`
- `education_translations`
- `profile_translations`

## 3. Response chuẩn cho entity có translation

Ví dụ:

```json
{
  "title": "English title",
  "locale": "en",
  "translated": true
}
```

Ý nghĩa:

- `locale`: locale backend đang resolve
- `translated=true`: có translation thật
- `translated=false`: đang fallback về document gốc

## 4. Cách FE nên dùng

Public pages:

- luôn gửi `?lang=vi` hoặc `?lang=en`
- dùng `translated` để biết có nên hiển thị badge/fallback logic riêng hay không

Admin CMS:

- lưu nội dung gốc bằng payload chính
- sửa từng bản dịch bằng endpoint riêng:
  - `PUT /api/v1/admin/posts/{id}/translations/{locale}`
  - `PUT /api/v1/admin/projects/{id}/translations/{locale}`
  - `PUT /api/v1/admin/experiences/{id}/translations/{locale}`
  - `PUT /api/v1/educations/{id}/translations/{locale}`
  - `PUT /api/v1/admin/profile/translations/{locale}`

## 5. Translation Payload

Ví dụ với `post`:

```json
{
  "title": "English title",
  "summary": "English summary",
  "content": "English content",
  "titleSeo": "English SEO title",
  "descriptionSeo": "English SEO description",
  "seoKeywords": ["spring", "java"]
}
```

Ví dụ với `profile`:

```json
{
  "fullName": "Wuu Bang Dev",
  "title": "Full Stack Developer",
  "bio": "English bio",
  "location": "Da Nang"
}
```

## 6. Fallback Rule

Nếu request `?lang=en` nhưng chưa có translation:

- backend vẫn trả dữ liệu gốc
- `locale` vẫn là `en`
- `translated` sẽ là `false`

Điều này giúp FE biết:

- user đang xem locale nào
- dữ liệu có phải bản dịch thật hay không
