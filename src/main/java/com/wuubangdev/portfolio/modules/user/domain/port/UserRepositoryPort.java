package com.wuubangdev.portfolio.modules.user.domain.port;

import com.wuubangdev.portfolio.modules.user.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);
    List<User> findAll();
    User save(User user);
}