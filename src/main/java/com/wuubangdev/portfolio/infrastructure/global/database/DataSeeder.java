package com.wuubangdev.portfolio.infrastructure.global.database;

import com.wuubangdev.portfolio.modules.user.domain.model.Role;
import com.wuubangdev.portfolio.modules.user.domain.model.User;
import com.wuubangdev.portfolio.modules.user.domain.model.UserType;
import com.wuubangdev.portfolio.modules.user.domain.port.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    // Tuân thủ: Chỉ gọi Port, không gọi Repository trực tiếp
    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Kiểm tra xem đã có admin chưa (Sử dụng Port để tìm kiếm)
        userRepositoryPort.findByUsername("admin").ifPresentOrElse(
                user -> log.info("Admin user already exists. Skipping seeding."),
                () -> {
                    log.info("Seeding default admin user...");

                    // Tạo Domain Model sạch
                    User admin = User.builder()
                            .username("admin")
                            .email("admin@wuubangdev.com")
                            .password(passwordEncoder.encode("admin123"))
                            .roles(List.of(Role.ROLE_ADMIN, Role.ROLE_USER))
                            .enabled(true)
                            .userType(UserType.BASIC)
                            .build();

                    // Lưu thông qua Port
                    userRepositoryPort.save(admin);

                    log.info("Admin user seeded successfully: admin / admin123");
                }
        );
    }
}
