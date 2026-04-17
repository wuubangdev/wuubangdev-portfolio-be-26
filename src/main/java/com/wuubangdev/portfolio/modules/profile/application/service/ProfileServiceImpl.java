package com.wuubangdev.portfolio.modules.profile.application.service;

import com.wuubangdev.portfolio.modules.profile.application.dto.ProfileRequest;
import com.wuubangdev.portfolio.modules.profile.application.dto.ProfileResponse;
import com.wuubangdev.portfolio.modules.profile.application.dto.ProfileTranslationRequest;
import com.wuubangdev.portfolio.modules.profile.application.mapper.ProfileApplicationMapper;
import com.wuubangdev.portfolio.modules.profile.domain.model.Profile;
import com.wuubangdev.portfolio.modules.profile.domain.port.ProfileRepositoryPort;
import com.wuubangdev.portfolio.modules.profile.domain.model.ProfileTranslation;
import com.wuubangdev.portfolio.modules.profile.domain.port.ProfileTranslationRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepositoryPort profileRepositoryPort;
    private final ProfileTranslationRepositoryPort profileTranslationRepositoryPort;
    private final ProfileApplicationMapper profileApplicationMapper;

    @Override
    public ProfileResponse getProfile() {
        String locale = resolveLocaleCode();
        return profileRepositoryPort.findFirst()
                .map(profile -> toLocalizedResponse(profile, locale))
                .orElseGet(() -> profileApplicationMapper.defaultResponse(locale));
    }

    @Override
    @Transactional
    public ProfileResponse upsertProfile(ProfileRequest request) {
        Profile profile = profileRepositoryPort.findFirst()
                .orElseGet(Profile::new);

        Profile mappedProfile = profileApplicationMapper.toDomain(request, profile);
        Profile savedProfile = profileRepositoryPort.save(mappedProfile);
        syncTranslations(savedProfile.getId(), request);
        return toLocalizedResponse(savedProfile, resolveLocaleCode());
    }

    @Override
    @Transactional
    public ProfileResponse upsertTranslation(String locale, ProfileTranslationRequest request) {
        Profile profile = profileRepositoryPort.findFirst()
                .orElseThrow(() -> new IllegalStateException("Profile not found"));
        profileTranslationRepositoryPort.save(ProfileTranslation.builder()
                .profileId(profile.getId())
                .locale(normalizeLocale(locale))
                .fullName(request.fullName())
                .title(request.title())
                .bio(request.bio())
                .location(request.location())
                .build());
        return toLocalizedResponse(profile, normalizeLocale(locale));
    }

    private void syncTranslations(Long profileId, ProfileRequest request) {
        if (request.translations() == null || request.translations().isEmpty()) {
            return;
        }

        List<ProfileTranslation> translations = request.translations().stream()
                .map(translation -> ProfileTranslation.builder()
                        .profileId(profileId)
                        .locale(normalizeLocale(translation.locale()))
                        .fullName(translation.fullName())
                        .title(translation.title())
                        .bio(translation.bio())
                        .location(translation.location())
                        .build())
                .toList();

        profileTranslationRepositoryPort.saveAll(translations);
    }

    private ProfileResponse toLocalizedResponse(Profile profile, String locale) {
        ProfileTranslation translation = profileTranslationRepositoryPort.findByProfileIdAndLocale(profile.getId(), locale)
                .orElse(null);
        return profileApplicationMapper.toResponse(profileApplicationMapper.applyTranslation(profile, translation), locale, translation != null);
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
