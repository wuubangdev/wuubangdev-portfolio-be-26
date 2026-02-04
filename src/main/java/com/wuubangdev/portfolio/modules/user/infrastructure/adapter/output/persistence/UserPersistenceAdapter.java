package com.wuubangdev.portfolio.modules.user.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.modules.user.domain.model.User;
import com.wuubangdev.portfolio.modules.user.domain.port.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserRepositoryPort {

    private final UserJpaRepository jpaRepository;
    private final UserMapper userMapper; // Inject Mapper vào đây

    @Override
    public Optional<User> findByUsername(String username) {
        return jpaRepository.findByUsername(username)
                .map(userMapper::toDomain); // Dùng mapper để chuyển đổi
    }

    @Override
    public void save(User user) {
        UserJpaEntity entity = userMapper.toEntity(user);
        jpaRepository.save(entity);
    }
}