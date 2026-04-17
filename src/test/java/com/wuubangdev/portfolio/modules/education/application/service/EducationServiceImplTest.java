package com.wuubangdev.portfolio.modules.education.application.service;

import com.wuubangdev.portfolio.modules.education.application.dto.EducationResponse;
import com.wuubangdev.portfolio.modules.education.domain.model.Education;
import com.wuubangdev.portfolio.modules.education.domain.model.EducationTranslation;
import com.wuubangdev.portfolio.modules.education.domain.port.EducationRepositoryPort;
import com.wuubangdev.portfolio.modules.education.domain.port.EducationTranslationRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class EducationServiceImplTest {

    private InMemoryEducationRepository repository;
    private InMemoryEducationTranslationRepository translationRepository;
    private EducationServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryEducationRepository();
        translationRepository = new InMemoryEducationTranslationRepository();
        service = new EducationServiceImpl(repository, translationRepository);
        LocaleContextHolder.setLocale(Locale.forLanguageTag("vi"));
    }

    @Test
    void getEducationByIdShouldReturnTranslatedFieldsForCurrentLocale() {
        repository.educations.add(education(1L, "Dai hoc", "Ky su", "CNTT", "Mo ta", "Da Nang"));
        translationRepository.translations.add(EducationTranslation.builder()
                .id(20L)
                .educationId(1L)
                .locale("en")
                .institution("University")
                .degree("Engineer")
                .fieldOfStudy("Computer Science")
                .description("Description")
                .location("Da Nang")
                .build());
        LocaleContextHolder.setLocale(Locale.ENGLISH);

        EducationResponse response = service.getEducationById(1L);

        assertThat(response.institution()).isEqualTo("University");
        assertThat(response.degree()).isEqualTo("Engineer");
        assertThat(response.fieldOfStudy()).isEqualTo("Computer Science");
        assertThat(response.description()).isEqualTo("Description");
        assertThat(response.locale()).isEqualTo("en");
        assertThat(response.translated()).isTrue();
    }

    private Education education(Long id, String institution, String degree, String fieldOfStudy, String description, String location) {
        return Education.builder()
                .id(id)
                .institution(institution)
                .degree(degree)
                .fieldOfStudy(fieldOfStudy)
                .description(description)
                .location(location)
                .build();
    }

    private static class InMemoryEducationRepository implements EducationRepositoryPort {
        private final List<Education> educations = new ArrayList<>();

        @Override
        public Education save(Education education) {
            educations.removeIf(current -> current.getId().equals(education.getId()));
            educations.add(education);
            return education;
        }

        @Override
        public List<Education> findAll() {
            return List.copyOf(educations);
        }

        @Override
        public Optional<Education> findById(Long id) {
            return educations.stream().filter(education -> id.equals(education.getId())).findFirst();
        }

        @Override
        public void deleteById(Long id) {
            educations.removeIf(education -> id.equals(education.getId()));
        }
    }

    private static class InMemoryEducationTranslationRepository implements EducationTranslationRepositoryPort {
        private final List<EducationTranslation> translations = new ArrayList<>();

        @Override
        public EducationTranslation save(EducationTranslation translation) {
            translations.removeIf(current ->
                    current.getEducationId().equals(translation.getEducationId()) &&
                            current.getLocale().equals(translation.getLocale()));
            translations.add(translation);
            return translation;
        }

        @Override
        public List<EducationTranslation> saveAll(List<EducationTranslation> translations) {
            translations.forEach(this::save);
            return translations;
        }

        @Override
        public Optional<EducationTranslation> findByEducationIdAndLocale(Long educationId, String locale) {
            return translations.stream()
                    .filter(translation -> educationId.equals(translation.getEducationId()) && locale.equals(translation.getLocale()))
                    .findFirst();
        }

        @Override
        public List<EducationTranslation> findByEducationIdInAndLocale(List<Long> educationIds, String locale) {
            return translations.stream()
                    .filter(translation -> educationIds.contains(translation.getEducationId()) && locale.equals(translation.getLocale()))
                    .toList();
        }

        @Override
        public void deleteByEducationId(Long educationId) {
            translations.removeIf(translation -> educationId.equals(translation.getEducationId()));
        }
    }
}
