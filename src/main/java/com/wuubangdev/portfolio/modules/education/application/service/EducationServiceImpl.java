package com.wuubangdev.portfolio.modules.education.application.service;

import com.wuubangdev.portfolio.modules.education.application.dto.EducationRequest;
import com.wuubangdev.portfolio.modules.education.application.dto.EducationResponse;
import com.wuubangdev.portfolio.modules.education.application.dto.EducationTranslationRequest;
import com.wuubangdev.portfolio.modules.education.domain.model.Education;
import com.wuubangdev.portfolio.modules.education.domain.model.EducationTranslation;
import com.wuubangdev.portfolio.modules.education.domain.port.EducationRepositoryPort;
import com.wuubangdev.portfolio.modules.education.domain.port.EducationTranslationRepositoryPort;
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
public class EducationServiceImpl implements EducationService {

    private final EducationRepositoryPort educationRepositoryPort;
    private final EducationTranslationRepositoryPort educationTranslationRepositoryPort;

    @Override
    public List<EducationResponse> getAllEducations() {
        return toLocalizedResponses(educationRepositoryPort.findAll(), resolveLocaleCode());
    }

    @Override
    public EducationResponse getEducationById(Long id) {
        return educationRepositoryPort.findById(id)
                .map(education -> toLocalizedResponse(education, resolveLocaleCode()))
                .orElseThrow(() -> new RuntimeException("Education not found"));
    }

    @Override
    @Transactional
    public EducationResponse createEducation(EducationRequest request) {
        Education education = Education.builder()
                .institution(request.institution())
                .degree(request.degree())
                .fieldOfStudy(request.fieldOfStudy())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .description(request.description())
                .logoUrl(request.logoUrl())
                .location(request.location())
                .displayOrder(request.displayOrder())
                .isPublic(request.isPublic())
                .build();
        Education savedEducation = educationRepositoryPort.save(education);
        syncTranslations(savedEducation.getId(), request);
        return toLocalizedResponse(savedEducation, resolveLocaleCode());
    }

    @Override
    @Transactional
    public EducationResponse updateEducation(Long id, EducationRequest request) {
        Education education = educationRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Education not found"));
        education.setInstitution(request.institution());
        education.setDegree(request.degree());
        education.setFieldOfStudy(request.fieldOfStudy());
        education.setStartDate(request.startDate());
        education.setEndDate(request.endDate());
        education.setDescription(request.description());
        education.setLogoUrl(request.logoUrl());
        education.setLocation(request.location());
        education.setDisplayOrder(request.displayOrder());
        education.setIsPublic(request.isPublic());
        Education savedEducation = educationRepositoryPort.save(education);
        syncTranslations(savedEducation.getId(), request);
        return toLocalizedResponse(savedEducation, resolveLocaleCode());
    }

    @Override
    @Transactional
    public EducationResponse upsertTranslation(Long id, String locale, EducationTranslationRequest request) {
        Education education = educationRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Education not found"));
        educationTranslationRepositoryPort.save(EducationTranslation.builder()
                .educationId(id)
                .locale(normalizeLocale(locale))
                .institution(request.institution())
                .degree(request.degree())
                .fieldOfStudy(request.fieldOfStudy())
                .description(request.description())
                .location(request.location())
                .build());
        return toLocalizedResponse(education, normalizeLocale(locale));
    }

    @Override
    @Transactional
    public void deleteEducation(Long id) {
        educationRepositoryPort.deleteById(id);
        educationTranslationRepositoryPort.deleteByEducationId(id);
    }

    @Override
    @Transactional
    public EducationResponse setOrder(Long id, Integer order) {
        Education education = educationRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Education not found"));
        education.setDisplayOrder(order);
        return toLocalizedResponse(educationRepositoryPort.save(education), resolveLocaleCode());
    }

    @Override
    @Transactional
    public EducationResponse togglePublic(Long id, Boolean isPublic) {
        Education education = educationRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Education not found"));
        education.setIsPublic(isPublic);
        return toLocalizedResponse(educationRepositoryPort.save(education), resolveLocaleCode());
    }

    private void syncTranslations(Long educationId, EducationRequest request) {
        if (request.translations() == null || request.translations().isEmpty()) {
            return;
        }

        List<EducationTranslation> translations = request.translations().stream()
                .map(translation -> EducationTranslation.builder()
                        .educationId(educationId)
                        .locale(normalizeLocale(translation.locale()))
                        .institution(translation.institution())
                        .degree(translation.degree())
                        .fieldOfStudy(translation.fieldOfStudy())
                        .description(translation.description())
                        .location(translation.location())
                        .build())
                .toList();

        educationTranslationRepositoryPort.saveAll(translations);
    }

    private List<EducationResponse> toLocalizedResponses(List<Education> educations, String locale) {
        if (educations.isEmpty()) {
            return List.of();
        }

        List<Long> educationIds = educations.stream().map(Education::getId).toList();
        Map<Long, EducationTranslation> translationMap = new HashMap<>();
        educationTranslationRepositoryPort.findByEducationIdInAndLocale(educationIds, locale)
                .forEach(translation -> translationMap.put(translation.getEducationId(), translation));

        return educations.stream()
                .map(education -> toResponse(
                        applyTranslation(education, translationMap.get(education.getId())),
                        locale,
                        translationMap.get(education.getId()) != null))
                .toList();
    }

    private EducationResponse toLocalizedResponse(Education education, String locale) {
        EducationTranslation translation = educationTranslationRepositoryPort.findByEducationIdAndLocale(education.getId(), locale)
                .orElse(null);
        return toResponse(applyTranslation(education, translation), locale, translation != null);
    }

    private Education applyTranslation(Education education, EducationTranslation translation) {
        if (translation == null) {
            return education;
        }

        return Education.builder()
                .id(education.getId())
                .institution(defaultIfBlank(translation.getInstitution(), education.getInstitution()))
                .degree(defaultIfBlank(translation.getDegree(), education.getDegree()))
                .fieldOfStudy(defaultIfBlank(translation.getFieldOfStudy(), education.getFieldOfStudy()))
                .startDate(education.getStartDate())
                .endDate(education.getEndDate())
                .description(defaultIfBlank(translation.getDescription(), education.getDescription()))
                .logoUrl(education.getLogoUrl())
                .location(defaultIfBlank(translation.getLocation(), education.getLocation()))
                .displayOrder(education.getDisplayOrder())
                .isPublic(education.getIsPublic())
                .build();
    }

    private EducationResponse toResponse(Education education, String locale, boolean translated) {
        return new EducationResponse(
                education.getId(),
                education.getInstitution(),
                education.getDegree(),
                education.getFieldOfStudy(),
                education.getStartDate(),
                education.getEndDate(),
                education.getDescription(),
                education.getLogoUrl(),
                education.getLocation(),
                education.getDisplayOrder(),
                education.getIsPublic(),
                locale,
                translated
        );
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

    private String defaultIfBlank(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
