package com.wuubangdev.portfolio.modules.user.infrastructure.adapter.output.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserJpaRepository extends MongoRepository<UserJpaEntity, Long> {

    // Tìm kiếm user theo username để phục vụ Security
    Optional<UserJpaEntity> findByUsername(String username);
    Optional<UserJpaEntity> findByEmail(String email);
    Optional<UserJpaEntity> findByActivationToken(String activationToken);
    Optional<UserJpaEntity> findByResetPasswordToken(String resetPasswordToken);
    Optional<UserJpaEntity> findByRefreshToken(String refreshToken);

    // Kiểm tra tồn tại để phục vụ logic Đăng ký
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
