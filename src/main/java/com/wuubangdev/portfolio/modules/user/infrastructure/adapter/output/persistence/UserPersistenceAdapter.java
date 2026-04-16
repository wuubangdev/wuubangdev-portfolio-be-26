package com.wuubangdev.portfolio.modules.user.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.infrastructure.global.database.MongoSequenceService;
import com.wuubangdev.portfolio.modules.user.domain.model.User;
import com.wuubangdev.portfolio.modules.user.domain.port.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserRepositoryPort {

    private static final String SEQUENCE_NAME = "users_sequence";

    private final UserJpaRepository jpaRepository;
    private final UserMapper userMapper;
    private final MongoSequenceService sequenceService;

    @Override
    public Optional<User> findByUsername(String username) {
        return jpaRepository.findByUsername(username)
                .map(userMapper::toDomain); // Dùng mapper để chuyển đổi
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaRepository.findById(id)
                .map(userMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return jpaRepository.findAll().stream()
                .map(userMapper::toDomain)
                .toList();
    }

    @Override
    public User save(User user) {
        UserJpaEntity entity = userMapper.toEntity(user);
        if (entity.getId() == null) {
            entity.setId(sequenceService.nextId(SEQUENCE_NAME));
        }
        UserJpaEntity savedEntity = jpaRepository.save(entity);
        return userMapper.toDomain(savedEntity);
    }
}
