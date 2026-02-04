package com.wuubangdev.portfolio.modules.user.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.modules.user.domain.model.User;
import org.springframework.stereotype.Component;
import java.util.ArrayList;

@Component
public class UserMapper {

    /**
     * Chuyển đổi từ JPA Entity sang Domain Model
     */
    public User toDomain(UserJpaEntity entity) {
        if (entity == null) return null;

        return User.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .email(entity.getEmail())
                // Đảm bảo roles trong Domain User cũng đã đổi thành List
                .roles(entity.getRoles() != null ? new ArrayList<>(entity.getRoles()) : new ArrayList<>())
                .build();
    }

    /**
     * Chuyển đổi từ Domain Model sang JPA Entity
     */
    public UserJpaEntity toEntity(User domain) {
        if (domain == null) return null;

        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(domain.getId());
        entity.setUsername(domain.getUsername());
        entity.setPassword(domain.getPassword());
        entity.setEmail(domain.getEmail());
        // Chuyển đổi sang List cho Entity để thỏa mãn @OrderColumn
        entity.setRoles(domain.getRoles() != null ? new ArrayList<>(domain.getRoles()) : new ArrayList<>());

        return entity;
    }
}