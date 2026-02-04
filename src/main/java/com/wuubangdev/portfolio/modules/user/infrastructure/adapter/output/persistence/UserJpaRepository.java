package com.wuubangdev.portfolio.modules.user.infrastructure.adapter.output.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {

    // Tìm kiếm user theo username để phục vụ Security
    Optional<UserJpaEntity> findByUsername(String username);

    // Kiểm tra tồn tại để phục vụ logic Đăng ký
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}