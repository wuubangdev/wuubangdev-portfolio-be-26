# Wuubangdev Portfolio Backend

Backend cho portfolio website và admin CMS, xây bằng Java 21 + Spring Boot + MongoDB.

## Stack

- Java 21
- Spring Boot 3.4.x
- Spring Security + JWT
- Spring Data MongoDB
- Maven
- Swagger / OpenAPI

## Những gì backend đang có

- Auth local + social login Google/GitHub
- Refresh token
- Activate account qua email
- Forgot password / reset password qua email
- Contact form + email thông báo
- CRUD cho profile, skill, experience, education, post, project, social, setting
- Post engagement: like, unlike, share
- SEO fields cho `post` và `project`
- Multilingual content cho:
  - `post`
  - `project`
  - `experience`
  - `education`
  - `profile`

## Chạy local

### Yêu cầu

- JDK 21
- MongoDB

### Run

```bash
./mvnw spring-boot:run
```

App mặc định chạy ở:

```text
http://localhost:8080
```

## Test

```bash
./mvnw test
```

## Swagger

```text
http://localhost:8080/swagger-ui/index.html
```

## CORS hiện tại

Đang allow:

- `http://localhost:3000`
- `https://*.vercel.app`
- `https://*.wuubangdev.com`

Nếu FE dev chạy domain khác như `http://localhost:5173` thì cần thêm lại CORS.

## i18n

Hỗ trợ đổi locale bằng query param:

```http
?lang=vi
?lang=en
```

Entity có translation sẽ trả thêm:

- `locale`
- `translated`

## Tài liệu quan trọng

- [API_DOCUMENTATION.md](./API_DOCUMENTATION.md)
- [API_RESPONSE_STANDARD.md](./API_RESPONSE_STANDARD.md)
- [I18N_README.md](./I18N_README.md)

## Ghi chú cho Frontend

- Không phải mọi endpoint đều trả response wrapper
- Cần đọc đúng contract theo tài liệu API
- Với các entity đa ngôn ngữ:
  - dùng `?lang=...`
  - đọc `translated` để biết đang là bản dịch thật hay fallback
- Với admin CMS:
  - có thể lưu dữ liệu gốc trong payload chính
  - có thể sửa từng locale bằng endpoint `PUT .../translations/{locale}`
