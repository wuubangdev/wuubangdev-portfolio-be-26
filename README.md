# 🚀 Wuubangdev Portfolio - Backend (Hexagonal Architecture)

Dự án Backend cho Portfolio cá nhân, được xây dựng trên nền tảng **Java 21** và **Spring Boot 4.x**. Hệ thống áp dụng kiến trúc **Hexagonal (Ports and Adapters)** để tách biệt hoàn toàn logic nghiệp vụ khỏi các chi tiết công nghệ, giúp dễ dàng mở rộng và bảo trì.



---

## 🏗️ Kiến trúc dự án (Project Architecture)

Dự án tuân thủ nguyên tắc **Dependency Rule**: Sự phụ thuộc chỉ hướng vào bên trong (Domain).

### 📁 Cấu trúc thư mục (Package Structure)

```text
src/main/java/com/wuubangdev/portfolio
│
├── common/                     # Tiện ích thuần Java, không dính Spring
│   ├── exception/              # Global Business Exceptions
│   └── util/                   # DateUtils, StringUtils, v.v.
│
├── infrastructure/             # GLOBAL INFRASTRUCTURE (Cấu hình chung toàn hệ thống)
│   ├── security/               # Security Config
│   │   ├── config/             # WebSecurityConfig.java, PasswordEncoder.java
│   │   ├── jwt/                # JwtTokenProvider.java, JwtFilter.java
│   │   └── userdetail/         # CustomUserDetailsService.java
│   ├── database/               # Cấu hình DataSource, Transaction, Auditing
│   ├── swagger/                # Cấu hình OpenAPI/Swagger
│   └── advice/                 # GlobalExceptionHandler.java (Bắt lỗi từ Controller)
│
└── modules/                    # CHIA THEO MODULE NGHIỆP VỤ (Local Infrastructure)
    └── [module_name]/
        ├── domain/             # Core Logic
        ├── application/        # Use Cases
        └── infrastructure/     # Local Config (Chỉ chứa Bean riêng của module đó)
            ├── adapter/
            └── config/         # Cấu hình Bean riêng (e.g., ModuleOrderConfig.java)
```

---

## 🛠️ Stack công nghệ (Tech Stack)

* **Language:** Java 21 (LTS)
* **Framework:** Spring Boot 4.0.2
* **Security:** Spring Security (Authentication & Authorization)
* **Database:** MySQL (với MySQL Connector J)
* **ORM:** Spring Data JPA
* **Validation:** Hibernate Validator
* **Build Tool:** Maven

---

## ⚖️ Quy tắc phát triển (Development Rules)

Để giữ cho kiến trúc luôn "sạch", mọi thành viên (hoặc chính bạn) cần tuân thủ:

1. **Domain Isolation:** Lớp `domain` không được phép import bất kỳ package nào từ `application` hoặc `infrastructure`. Không sử dụng `@Service` hay `@Entity` trong domain.
2. **Port-Driven:** Các tương tác với bên ngoài (DB, Mail, API bên thứ 3) phải thông qua Interface định nghĩa tại Domain/Application layer.
3. **Data Mapping:** Không để `JPA Entity` đi ngược lên lớp `Controller`. Sử dụng `Mapper` để chuyển đổi qua lại giữa `Entity` <-> `Domain` <-> `DTO`.

---

## 🚀 Cài đặt & Khởi chạy (Setup & Run)

### 1. Cấu hình môi trường

Vì `application.properties` đã được loại bỏ khỏi Git để bảo mật, hãy tạo file mới:

```bash
cp src/main/resources/application.example.properties src/main/resources/application.properties

```

### 2. Cấu hình Database

Cập nhật thông tin kết nối MySQL trong file `.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/portfolio_db
spring.datasource.username=your_username
spring.datasource.password=your_password

```

### 3. Build và Chạy

```bash
mvn clean install
mvn spring-boot:run

```

---

## 🧪 Kiểm thử (Testing)

Dự án tích hợp sẵn bộ công cụ test cho từng layer:

* **Unit Test:** JUnit 5 & Mockito cho Domain/Application.
* **Integration Test:** Spring Security Test & WebMvcTest cho Infrastructure.

---

*Created with ❤️ by **Wuubangdev***