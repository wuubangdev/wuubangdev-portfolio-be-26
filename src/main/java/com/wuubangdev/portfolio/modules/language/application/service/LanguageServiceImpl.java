package com.wuubangdev.portfolio.modules.language.application.service;

import com.wuubangdev.portfolio.modules.language.application.dto.LanguageRequest;
import com.wuubangdev.portfolio.modules.language.application.dto.LanguageResponse;
import com.wuubangdev.portfolio.modules.language.domain.model.Language;
import com.wuubangdev.portfolio.modules.language.domain.port.LanguageRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepositoryPort languageRepositoryPort;

    @Override
    public List<LanguageResponse> getAllLanguages() {
        return languageRepositoryPort.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public LanguageResponse getLanguageById(Long id) {
        return languageRepositoryPort.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Language not found"));
    }

    @Override
    @Transactional
    public LanguageResponse createLanguage(LanguageRequest request) {
        Language language = Language.builder()
                .code(request.code())
                .name(request.name())
                .isDefault(request.isDefault())
                .build();
        return toResponse(languageRepositoryPort.save(language));
    }

    @Override
    @Transactional
    public LanguageResponse updateLanguage(Long id, LanguageRequest request) {
        Language language = languageRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Language not found"));
        language.setCode(request.code());
        language.setName(request.name());
        language.setIsDefault(request.isDefault());
        return toResponse(languageRepositoryPort.save(language));
    }

    @Override
    @Transactional
    public void deleteLanguage(Long id) {
        languageRepositoryPort.deleteById(id);
    }

    private LanguageResponse toResponse(Language language) {
        return new LanguageResponse(
                language.getId(),
                language.getCode(),
                language.getName(),
                language.getIsDefault()
        );
    }
}
