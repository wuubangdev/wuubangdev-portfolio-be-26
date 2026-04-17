package com.wuubangdev.portfolio.modules.profile.application.service;

import com.wuubangdev.portfolio.modules.profile.application.dto.ProfileResponse;
import com.wuubangdev.portfolio.modules.profile.application.mapper.ProfileApplicationMapper;
import com.wuubangdev.portfolio.modules.profile.domain.model.Profile;
import com.wuubangdev.portfolio.modules.profile.domain.model.ProfileTranslation;
import com.wuubangdev.portfolio.modules.profile.domain.port.ProfileRepositoryPort;
import com.wuubangdev.portfolio.modules.profile.domain.port.ProfileTranslationRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ProfileServiceImplTest {

    private InMemoryProfileRepository repository;
    private InMemoryProfileTranslationRepository translationRepository;
    private ProfileServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryProfileRepository();
        translationRepository = new InMemoryProfileTranslationRepository();
        service = new ProfileServiceImpl(repository, translationRepository, new ProfileApplicationMapper());
        LocaleContextHolder.setLocale(Locale.forLanguageTag("vi"));
    }

    @Test
    void getProfileShouldReturnTranslatedFieldsForCurrentLocale() {
        repository.profile = Profile.builder()
                .id(1L)
                .fullName("Vu")
                .title("Lap trinh vien")
                .bio("Gioi thieu")
                .location("Ha Noi")
                .build();
        translationRepository.translations.add(ProfileTranslation.builder()
                .id(30L)
                .profileId(1L)
                .locale("en")
                .fullName("Vu")
                .title("Developer")
                .bio("Introduction")
                .location("Hanoi")
                .build());
        LocaleContextHolder.setLocale(Locale.ENGLISH);

        ProfileResponse response = service.getProfile();

        assertThat(response.title()).isEqualTo("Developer");
        assertThat(response.bio()).isEqualTo("Introduction");
        assertThat(response.location()).isEqualTo("Hanoi");
        assertThat(response.locale()).isEqualTo("en");
        assertThat(response.translated()).isTrue();
    }

    private static class InMemoryProfileRepository implements ProfileRepositoryPort {
        private Profile profile;

        @Override
        public Profile save(Profile profile) {
            this.profile = profile;
            return profile;
        }

        @Override
        public Optional<Profile> findFirst() {
            return Optional.ofNullable(profile);
        }
    }

    private static class InMemoryProfileTranslationRepository implements ProfileTranslationRepositoryPort {
        private final List<ProfileTranslation> translations = new ArrayList<>();

        @Override
        public ProfileTranslation save(ProfileTranslation translation) {
            translations.removeIf(current ->
                    current.getProfileId().equals(translation.getProfileId()) &&
                            current.getLocale().equals(translation.getLocale()));
            translations.add(translation);
            return translation;
        }

        @Override
        public List<ProfileTranslation> saveAll(List<ProfileTranslation> translations) {
            translations.forEach(this::save);
            return translations;
        }

        @Override
        public Optional<ProfileTranslation> findByProfileIdAndLocale(Long profileId, String locale) {
            return translations.stream()
                    .filter(translation -> profileId.equals(translation.getProfileId()) && locale.equals(translation.getLocale()))
                    .findFirst();
        }

        @Override
        public void deleteByProfileId(Long profileId) {
            translations.removeIf(translation -> profileId.equals(translation.getProfileId()));
        }
    }
}
