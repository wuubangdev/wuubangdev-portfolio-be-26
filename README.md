# Wuubangdev Portfolio Backend

Backend cho portfolio và admin CMS, xây bằng Java 21 + Spring Boot 3.4.3. Dự án cung cấp API cho profile, skills, experiences, blog posts, products/projects, contact form, authentication bằng JWT, và upload ảnh local.

## Tính năng chính

- API public cho portfolio frontend
- API admin được bảo vệ bằng JWT + Spring Security
- CRUD cho `profile`, `skills`, `experiences`, `posts`, `projects`
- Contact form và màn quản trị contacts
- Upload ảnh local qua `/api/v1/admin/uploads/images`
- Seed sẵn tài khoản admin mặc định khi database còn trống
- Có `docker-compose` ở thư mục root để chạy full stack

## Tech stack

- Java 21
- Spring Boot 3.4.3
- Spring Security
- Spring Data MongoDB
- MongoDB Atlas
- JWT (`jjwt`)
- springdoc OpenAPI / Swagger UI
- Maven

## Kiến trúc

Dự án đang đi theo hướng hexagonal / ports and adapters:

- `modules/*/domain`: domain model và repository port
- `modules/*/application`: service, dto, mapper
- `modules/*/infrastructure`: controller, persistence adapter, Mongo document
- `infrastructure/global`: security, exception handler, config, upload, database

Một số refactor gần đây đã tách `Application Mapper` ra khỏi service để service tập trung điều phối nghiệp vụ hơn.

## Cấu trúc thư mục

```text
src/main/java/com/wuubangdev/portfolio
├── infrastructure
│   ├── global
│   │   ├── advice
│   │   ├── config
│   │   ├── database
│   │   ├── exception
│   │   ├── security
│   │   ├── swagger
│   │   └── upload
│   └── persistence
│       └── base
└── modules
    ├── contact
    ├── experience
    ├── post
    ├── profile
    ├── project
    ├── skill
    └── user
```

## Yêu cầu môi trường

- JDK 21
- MongoDB Atlas hoặc MongoDB 6+
- Maven Wrapper (`./mvnw`) đã có sẵn

## Chạy local

### 1. Cấu hình biến môi trường

Backend đọc cấu hình trực tiếp từ biến môi trường. Nếu không set gì, app sẽ dùng default local trong `src/main/resources/application.properties`.

Các biến quan trọng:

```bash
export SERVER_PORT=8080
export SPRING_DATA_MONGODB_URI='mongodb+srv://wuubangdev:<db_password>@cluster0.tfees4i.mongodb.net/wuu_db?retryWrites=true&w=majority&appName=Cluster0'
export SPRING_DATA_MONGODB_DATABASE='wuu_db'
export SPRING_DATA_MONGODB_AUTO_INDEX_CREATION=true
export APP_JWT_SECRET='change-this-super-secret-key-for-production'
export APP_JWT_EXPIRATION_MS=86400000
export APP_UPLOAD_DIR=uploads
```

### 2. Chạy ứng dụng

```bash
./mvnw spring-boot:run
```

Backend mặc định chạy ở `http://localhost:8080`.

## Build và kiểm tra

```bash
./mvnw -DskipTests compile
./mvnw test
```

Lưu ý: test khởi động Spring context nên cần MongoDB URI hợp lệ hoặc cấu hình test riêng.

## Docker

Từ thư mục root của workspace:

```bash
docker compose up --build
```

Khi đó:

- Backend: `http://localhost:8080`
- MongoDB: dùng theo `SPRING_DATA_MONGODB_URI`

## Swagger / OpenAPI

Khi app chạy, có thể mở:

- `http://localhost:8080/swagger-ui/index.html`

## Tài khoản admin mặc định

Nếu database chưa có user `admin`, hệ thống sẽ tự seed:

- Username: `admin`
- Password: `admin123`

Nên đổi thông tin này khi deploy thật.

## Các nhóm API chính

- Auth: `/api/v1/auth/*`
- Public profile: `/api/v1/profile`
- Public skills: `/api/v1/skills`
- Public experiences: `/api/v1/experiences`
- Public posts: `/api/v1/posts`
- Public projects/products: `/api/v1/projects`
- Contact form: `/api/v1/contact`
- Admin routes: `/api/v1/admin/*`

## Upload ảnh

- Endpoint: `POST /api/v1/admin/uploads/images`
- Cần token admin
- File sau khi upload được serve qua `/uploads/**`

## Ghi chú triển khai

- `spring.data.mongodb.auto-index-creation=true` tiện cho dev để tạo unique index tự động; production nên quản lý index rõ ràng hơn.
- `APP_UPLOAD_DIR` nên mount volume riêng khi chạy Docker hoặc deploy server.
- `APP_JWT_SECRET` cần thay bằng secret mạnh ở production.
- Hệ thống hiện giữ `Long id` ở API và sinh tự động qua collection `database_sequences` trong MongoDB.
