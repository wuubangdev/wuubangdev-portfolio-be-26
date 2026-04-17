package com.wuubangdev.portfolio.modules.experience.application.service;

import com.wuubangdev.portfolio.infrastructure.global.exception.ResourceNotFoundException;
import com.wuubangdev.portfolio.modules.experience.application.dto.ExperienceRequest;
import com.wuubangdev.portfolio.modules.experience.application.dto.ExperienceResponse;
import com.wuubangdev.portfolio.modules.experience.application.dto.ExperienceTranslationRequest;
import com.wuubangdev.portfolio.modules.experience.application.mapper.ExperienceApplicationMapper;
import com.wuubangdev.portfolio.modules.experience.domain.model.Experience;
import com.wuubangdev.portfolio.modules.experience.domain.model.ExperienceTranslation;
import com.wuubangdev.portfolio.modules.experience.domain.port.ExperienceRepositoryPort;
import com.wuubangdev.portfolio.modules.experience.domain.port.ExperienceTranslationRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExperienceServiceImpl implements ExperienceService {

    private final ExperienceRepositoryPort experienceRepositoryPort;
    private final ExperienceTranslationRepositoryPort experienceTranslationRepositoryPort;
    private final ExperienceApplicationMapper experienceApplicationMapper;

    @Override @Transactional
    public ExperienceResponse create(ExperienceRequest request) {
        Experience exp = experienceApplicationMapper.toNewDomain(request);
        Experience savedExperience = experienceRepositoryPort.save(exp);
        syncTranslations(savedExperience.getId(), request);
        return toLocalizedResponse(savedExperience, resolveLocaleCode());
    }

    @Override
    public List<ExperienceResponse> getAll() {
        return toLocalizedResponses(experienceRepositoryPort.findAll(), resolveLocaleCode());
    }

    @Override @Transactional
    public ExperienceResponse update(Long id, ExperienceRequest request) {
        Experience exp = experienceRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience", id));
        Experience updatedExperience = experienceApplicationMapper.updateDomain(exp, request);
        Experience savedExperience = experienceRepositoryPort.save(updatedExperience);
        syncTranslations(savedExperience.getId(), request);
        return toLocalizedResponse(savedExperience, resolveLocaleCode());
    }

    @Override
    @Transactional
    public ExperienceResponse upsertTranslation(Long id, String locale, ExperienceTranslationRequest request) {
        Experience experience = experienceRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience", id));
        experienceTranslationRepositoryPort.save(ExperienceTranslation.builder()
                .experienceId(id)
                .locale(normalizeLocale(locale))
                .company(request.company())
                .role(request.role())
                .description(request.description())
                .location(request.location())
                .build());
        return toLocalizedResponse(experience, normalizeLocale(locale));
    }

    @Override @Transactional
    public void delete(Long id) {
        experienceRepositoryPort.findById(id).orElseThrow(() -> new ResourceNotFoundException("Experience", id));
        experienceRepositoryPort.deleteById(id);
        experienceTranslationRepositoryPort.deleteByExperienceId(id);
    }

    private void syncTranslations(Long experienceId, ExperienceRequest request) {
        if (request.translations() == null || request.translations().isEmpty()) {
            return;
        }

        List<ExperienceTranslation> translations = request.translations().stream()
                .map(translation -> ExperienceTranslation.builder()
                        .experienceId(experienceId)
                        .locale(normalizeLocale(translation.locale()))
                        .company(translation.company())
                        .role(translation.role())
                        .description(translation.description())
                        .location(translation.location())
                        .build())
                .toList();

        experienceTranslationRepositoryPort.saveAll(translations);
    }

    private List<ExperienceResponse> toLocalizedResponses(List<Experience> experiences, String locale) {
        if (experiences.isEmpty()) {
            return List.of();
        }

        List<Long> experienceIds = experiences.stream().map(Experience::getId).toList();
        Map<Long, ExperienceTranslation> translationMap = new HashMap<>();
        experienceTranslationRepositoryPort.findByExperienceIdInAndLocale(experienceIds, locale)
                .forEach(translation -> translationMap.put(translation.getExperienceId(), translation));

        return experiences.stream()
                .map(experience -> experienceApplicationMapper.toResponse(
                        experienceApplicationMapper.applyTranslation(experience, translationMap.get(experience.getId())),
                        locale,
                        translationMap.get(experience.getId()) != null))
                .toList();
    }

    private ExperienceResponse toLocalizedResponse(Experience experience, String locale) {
        ExperienceTranslation translation = experienceTranslationRepositoryPort
                .findByExperienceIdAndLocale(experience.getId(), locale)
                .orElse(null);
        return experienceApplicationMapper.toResponse(
                experienceApplicationMapper.applyTranslation(experience, translation),
                locale,
                translation != null);
    }

    private String resolveLocaleCode() {
        return normalizeLocale(LocaleContextHolder.getLocale().getLanguage());
    }

    private String normalizeLocale(String locale) {
        if (locale == null || locale.isBlank()) {
            return "vi";
        }
        return locale.toLowerCase(Locale.ROOT);
    }
}
