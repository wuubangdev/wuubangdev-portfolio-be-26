package com.wuubangdev.portfolio.modules.user.domain.port;

import com.wuubangdev.portfolio.modules.user.domain.model.User;

import java.util.Optional;

public interface UserRepositoryPort {
    Optional<User> findByUsername(String username);
    void save(User user);
}