package com.wuubangdev.portfolio.modules.language.application.service;

import com.wuubangdev.portfolio.modules.language.application.dto.LanguageRequest;
import com.wuubangdev.portfolio.modules.language.application.dto.LanguageResponse;

import java.util.List;

public interface LanguageService {
    List<LanguageResponse> getAllLanguages();
    LanguageResponse getLanguageById(Long id);
    LanguageResponse createLanguage(LanguageRequest request);
    LanguageResponse updateLanguage(Long id, LanguageRequest request);
    void deleteLanguage(Long id);
}
