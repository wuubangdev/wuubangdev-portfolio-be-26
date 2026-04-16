# Internationalization (i18n) Implementation

## Tổng quan
Hệ thống hỗ trợ đa ngôn ngữ với Spring Boot i18n. Mặc định sử dụng tiếng Việt, hỗ trợ chuyển đổi sang tiếng Anh.

## Cấu hình
- **File cấu hình**: `I18nConfig.java`
- **Message files**: 
  - `messages.properties` (mặc định)
  - `messages_vi.properties` (tiếng Việt)
  - `messages_en.properties` (tiếng Anh)
- **Default locale**: `vi` (tiếng Việt)

## Cách sử dụng

### 1. Thay đổi ngôn ngữ
```
GET /api/v1/some-endpoint?lang=en
GET /api/v1/some-endpoint?lang=vi
```

### 2. Trong code
```java
@Autowired
private MessageSource messageSource;

// Lấy message theo locale hiện tại
String message = messageSource.getMessage("key", args, locale);
```

### 3. Các key message chính
- `auth.login.success` - Đăng nhập thành công
- `auth.register.success` - Đăng ký thành công
- `post.not.found` - Không tìm thấy bài viết
- `user.not.found` - Không tìm thấy người dùng
- `error.internal` - Lỗi máy chủ nội bộ

## API Endpoints hỗ trợ i18n
- Tất cả error responses
- Authentication responses
- User management responses
- Post management responses

## Thêm ngôn ngữ mới
1. Tạo file `messages_{locale}.properties`
2. Dịch tất cả các key từ file mặc định
3. Restart application
