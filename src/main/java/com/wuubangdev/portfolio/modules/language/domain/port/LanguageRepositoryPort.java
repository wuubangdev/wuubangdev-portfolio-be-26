package com.wuubangdev.portfolio.modules.language.domain.port;

import com.wuubangdev.portfolio.modules.language.domain.model.Language;

import java.util.List;
import java.util.Optional;

public interface LanguageRepositoryPort {
    Language save(Language language);
    List<Language> findAll();
    Optional<Language> findById(Long id);
    Optional<Language> findByCode(String code);
    void deleteById(Long id);
}
