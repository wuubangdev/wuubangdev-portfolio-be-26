package com.wuubangdev.portfolio.modules.profile.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.infrastructure.global.database.MongoSequenceService;
import com.wuubangdev.portfolio.modules.profile.domain.model.Profile;
import com.wuubangdev.portfolio.modules.profile.domain.port.ProfileRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProfilePersistenceAdapter implements ProfileRepositoryPort {

    private static final String SEQUENCE_NAME = "profiles_sequence";

    private final ProfileJpaRepository profileJpaRepository;
    private final ProfileMapper profileMapper;
    private final MongoSequenceService sequenceService;

    @Override
    public Profile save(Profile profile) {
        ProfileJpaEntity entity = profileMapper.toEntity(profile);
        if (entity.getId() == null) {
            entity.setId(sequenceService.nextId(SEQUENCE_NAME));
        }
        return profileMapper.toDomain(profileJpaRepository.save(entity));
    }

    @Override
    public Optional<Profile> findFirst() {
        return profileJpaRepository.findFirstByOrderByIdAsc()
                .map(profileMapper::toDomain);
    }
}
