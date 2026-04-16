package com.wuubangdev.portfolio.modules.social.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.infrastructure.global.database.MongoSequenceService;
import com.wuubangdev.portfolio.modules.social.domain.model.Social;
import com.wuubangdev.portfolio.modules.social.domain.port.SocialRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SocialPersistenceAdapter implements SocialRepositoryPort {

    private static final String SEQUENCE_NAME = "socials_sequence";

    private final SocialJpaRepository socialJpaRepository;
    private final SocialMapper socialMapper;
    private final MongoSequenceService sequenceService;

    @Override
    public Social save(Social social) {
        SocialJpaEntity entity = socialMapper.toEntity(social);
        if (entity.getId() == null) {
            entity.setId(sequenceService.nextId(SEQUENCE_NAME));
        }
        return socialMapper.toDomain(socialJpaRepository.save(entity));
    }

    @Override
    public Optional<Social> findFirst() {
        return socialJpaRepository.findFirstByOrderByIdAsc()
                .map(socialMapper::toDomain);
    }
}
