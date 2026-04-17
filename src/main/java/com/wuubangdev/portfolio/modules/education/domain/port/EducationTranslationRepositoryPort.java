package com.wuubangdev.portfolio.modules.education.domain.port;

import com.wuubangdev.portfolio.modules.education.domain.model.EducationTranslation;

import java.util.List;
import java.util.Optional;

public interface EducationTranslationRepositoryPort {
    EducationTranslation save(EducationTranslation translation);
    List<EducationTranslation> saveAll(List<EducationTranslation> translations);
    Optional<EducationTranslation> findByEducationIdAndLocale(Long educationId, String locale);
    List<EducationTranslation> findByEducationIdInAndLocale(List<Long> educationIds, String locale);
    void deleteByEducationId(Long educationId);
}
