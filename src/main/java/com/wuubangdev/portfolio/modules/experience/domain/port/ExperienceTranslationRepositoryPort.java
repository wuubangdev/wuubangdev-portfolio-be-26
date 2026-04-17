package com.wuubangdev.portfolio.modules.experience.domain.port;

import com.wuubangdev.portfolio.modules.experience.domain.model.ExperienceTranslation;

import java.util.List;
import java.util.Optional;

public interface ExperienceTranslationRepositoryPort {
    ExperienceTranslation save(ExperienceTranslation translation);
    List<ExperienceTranslation> saveAll(List<ExperienceTranslation> translations);
    Optional<ExperienceTranslation> findByExperienceIdAndLocale(Long experienceId, String locale);
    List<ExperienceTranslation> findByExperienceIdInAndLocale(List<Long> experienceIds, String locale);
    void deleteByExperienceId(Long experienceId);
}
