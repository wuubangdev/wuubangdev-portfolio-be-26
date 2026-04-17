package com.wuubangdev.portfolio.modules.experience.application.service;

import com.wuubangdev.portfolio.modules.experience.application.dto.ExperienceResponse;
import com.wuubangdev.portfolio.modules.experience.application.mapper.ExperienceApplicationMapper;
import com.wuubangdev.portfolio.modules.experience.domain.model.Experience;
import com.wuubangdev.portfolio.modules.experience.domain.model.ExperienceTranslation;
import com.wuubangdev.portfolio.modules.experience.domain.port.ExperienceRepositoryPort;
import com.wuubangdev.portfolio.modules.experience.domain.port.ExperienceTranslationRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ExperienceServiceImplTest {

    private InMemoryExperienceRepository repository;
    private InMemoryExperienceTranslationRepository translationRepository;
    private ExperienceServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryExperienceRepository();
        translationRepository = new InMemoryExperienceTranslationRepository();
        service = new ExperienceServiceImpl(repository, translationRepository, new ExperienceApplicationMapper());
        LocaleContextHolder.setLocale(Locale.forLanguageTag("vi"));
    }

    @Test
    void getAllShouldReturnTranslatedFieldsForCurrentLocale() {
        repository.experiences.add(experience(1L, "Cong ty", "Lap trinh vien", "Mo ta", "Ha Noi"));
        translationRepository.translations.add(ExperienceTranslation.builder()
                .id(10L)
                .experienceId(1L)
                .locale("en")
                .company("Company")
                .role("Developer")
                .description("Description")
                .location("Hanoi")
                .build());
        LocaleContextHolder.setLocale(Locale.ENGLISH);

        List<ExperienceResponse> responses = service.getAll();

        assertThat(responses).hasSize(1);
        assertThat(responses.getFirst().company()).isEqualTo("Company");
        assertThat(responses.getFirst().role()).isEqualTo("Developer");
        assertThat(responses.getFirst().description()).isEqualTo("Description");
        assertThat(responses.getFirst().locale()).isEqualTo("en");
        assertThat(responses.getFirst().translated()).isTrue();
    }

    private Experience experience(Long id, String company, String role, String description, String location) {
        return Experience.builder()
                .id(id)
                .company(company)
                .role(role)
                .description(description)
                .location(location)
                .build();
    }

    private static class InMemoryExperienceRepository implements ExperienceRepositoryPort {
        private final List<Experience> experiences = new ArrayList<>();

        @Override
        public Experience save(Experience exp) {
            experiences.removeIf(current -> current.getId().equals(exp.getId()));
            experiences.add(exp);
            return exp;
        }

        @Override
        public List<Experience> findAll() {
            return List.copyOf(experiences);
        }

        @Override
        public Optional<Experience> findById(Long id) {
            return experiences.stream().filter(experience -> id.equals(experience.getId())).findFirst();
        }

        @Override
        public void deleteById(Long id) {
            experiences.removeIf(experience -> id.equals(experience.getId()));
        }
    }

    private static class InMemoryExperienceTranslationRepository implements ExperienceTranslationRepositoryPort {
        private final List<ExperienceTranslation> translations = new ArrayList<>();

        @Override
        public ExperienceTranslation save(ExperienceTranslation translation) {
            translations.removeIf(current ->
                    current.getExperienceId().equals(translation.getExperienceId()) &&
                            current.getLocale().equals(translation.getLocale()));
            translations.add(translation);
            return translation;
        }

        @Override
        public List<ExperienceTranslation> saveAll(List<ExperienceTranslation> translations) {
            translations.forEach(this::save);
            return translations;
        }

        @Override
        public Optional<ExperienceTranslation> findByExperienceIdAndLocale(Long experienceId, String locale) {
            return translations.stream()
                    .filter(translation -> experienceId.equals(translation.getExperienceId()) && locale.equals(translation.getLocale()))
                    .findFirst();
        }

        @Override
        public List<ExperienceTranslation> findByExperienceIdInAndLocale(List<Long> experienceIds, String locale) {
            return translations.stream()
                    .filter(translation -> experienceIds.contains(translation.getExperienceId()) && locale.equals(translation.getLocale()))
                    .toList();
        }

        @Override
        public void deleteByExperienceId(Long experienceId) {
            translations.removeIf(translation -> experienceId.equals(translation.getExperienceId()));
        }
    }
}
