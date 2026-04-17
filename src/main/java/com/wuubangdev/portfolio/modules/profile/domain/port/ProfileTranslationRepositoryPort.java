package com.wuubangdev.portfolio.modules.profile.domain.port;

import com.wuubangdev.portfolio.modules.profile.domain.model.ProfileTranslation;

import java.util.List;
import java.util.Optional;

public interface ProfileTranslationRepositoryPort {
    ProfileTranslation save(ProfileTranslation translation);
    List<ProfileTranslation> saveAll(List<ProfileTranslation> translations);
    Optional<ProfileTranslation> findByProfileIdAndLocale(Long profileId, String locale);
    void deleteByProfileId(Long profileId);
}
