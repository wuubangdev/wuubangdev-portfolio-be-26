package com.wuubangdev.portfolio.modules.profile.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.infrastructure.global.database.MongoSequenceService;
import com.wuubangdev.portfolio.modules.profile.domain.model.ProfileTranslation;
import com.wuubangdev.portfolio.modules.profile.domain.port.ProfileTranslationRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProfileTranslationPersistenceAdapter implements ProfileTranslationRepositoryPort {

    private static final String SEQUENCE_NAME = "profile_translations_sequence";

    private final ProfileTranslationJpaRepository repository;
    private final MongoSequenceService sequenceService;

    @Override
    public ProfileTranslation save(ProfileTranslation translation) {
        ProfileTranslationJpaEntity entity = toEntity(translation);
        if (entity.getId() == null) {
            repository.findByProfileIdAndLocale(entity.getProfileId(), entity.getLocale())
                    .ifPresent(existing -> entity.setId(existing.getId()));
        }
        if (entity.getId() == null) {
            entity.setId(sequenceService.nextId(SEQUENCE_NAME));
        }
        return toDomain(repository.save(entity));
    }

    @Override
    public List<ProfileTranslation> saveAll(List<ProfileTranslation> translations) {
        return translations.stream().map(this::save).toList();
    }

    @Override
    public Optional<ProfileTranslation> findByProfileIdAndLocale(Long profileId, String locale) {
        return repository.findByProfileIdAndLocale(profileId, locale).map(this::toDomain);
    }

    @Override
    public void deleteByProfileId(Long profileId) {
        repository.deleteByProfileId(profileId);
    }

    private ProfileTranslation toDomain(ProfileTranslationJpaEntity entity) {
        return ProfileTranslation.builder()
                .id(entity.getId())
                .profileId(entity.getProfileId())
                .locale(entity.getLocale())
                .fullName(entity.getFullName())
                .title(entity.getTitle())
                .bio(entity.getBio())
                .location(entity.getLocation())
                .build();
    }

    private ProfileTranslationJpaEntity toEntity(ProfileTranslation domain) {
        ProfileTranslationJpaEntity entity = new ProfileTranslationJpaEntity();
        entity.setId(domain.getId());
        entity.setProfileId(domain.getProfileId());
        entity.setLocale(domain.getLocale());
        entity.setFullName(domain.getFullName());
        entity.setTitle(domain.getTitle());
        entity.setBio(domain.getBio());
        entity.setLocation(domain.getLocation());
        return entity;
    }
}
