package com.wuubangdev.portfolio.modules.user.domain.port;

import com.wuubangdev.portfolio.modules.user.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByActivationToken(String activationToken);
    Optional<User> findByResetPasswordToken(String resetPasswordToken);
    Optional<User> findByRefreshToken(String refreshToken);
    Optional<User> findById(Long id);
    List<User> findAll();
    User save(User user);
    boolean existsByEmail(String email);
}
