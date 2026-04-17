package com.wuubangdev.portfolio.modules.project.application.service;

import com.wuubangdev.portfolio.infrastructure.global.api.PageResponse;
import com.wuubangdev.portfolio.modules.project.application.dto.ProjectResponse;
import com.wuubangdev.portfolio.modules.project.application.mapper.ProjectApplicationMapper;
import com.wuubangdev.portfolio.modules.project.domain.model.Project;
import com.wuubangdev.portfolio.modules.project.domain.port.ProjectRepositoryPort;
import com.wuubangdev.portfolio.modules.project.domain.model.ProjectTranslation;
import com.wuubangdev.portfolio.modules.project.domain.port.ProjectTranslationRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectServiceImplTest {

    private InMemoryProjectRepository repository;
    private InMemoryProjectTranslationRepository translationRepository;
    private ProjectServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryProjectRepository();
        translationRepository = new InMemoryProjectTranslationRepository();
        service = new ProjectServiceImpl(repository, translationRepository, new ProjectApplicationMapper());
        LocaleContextHolder.setLocale(Locale.forLanguageTag("vi"));
    }

    @Test
    void getAllShouldUseFeaturedFilterWhenRequested() {
        repository.featuredProjects = List.of(project(1L, "featured-project", "Web", true));

        List<ProjectResponse> responses = service.getAll(null, true);

        assertThat(responses).hasSize(1);
        assertThat(responses.getFirst().slug()).isEqualTo("featured-project");
    }

    @Test
    void getAllShouldFilterByCategoryWhenProvided() {
        repository.categoryProjects = List.of(project(2L, "mobile-project", "Mobile", false));

        List<ProjectResponse> responses = service.getAll("Mobile", false);

        assertThat(responses).hasSize(1);
        assertThat(responses.getFirst().category()).isEqualTo("Mobile");
    }

    @Test
    void getByIdShouldReturnProjectDetail() {
        Project project = project(3L, "detail-project", "Backend", true);
        repository.projects.add(project);

        ProjectResponse response = service.getById(3L);

        assertThat(response.id()).isEqualTo(3L);
        assertThat(response.slug()).isEqualTo("detail-project");
        assertThat(response.translated()).isFalse();
    }

    @Test
    void getByIdShouldReturnTranslatedContentForCurrentLocale() {
        Project project = project(3L, "detail-project", "Backend", true);
        project.setDescription("Mo ta");
        project.setContent("Noi dung");
        repository.projects.add(project);
        translationRepository.translations.add(ProjectTranslation.builder()
                .id(30L)
                .projectId(3L)
                .locale("en")
                .title("Project detail")
                .description("Description")
                .content("Content")
                .build());
        LocaleContextHolder.setLocale(Locale.ENGLISH);

        ProjectResponse response = service.getById(3L);

        assertThat(response.title()).isEqualTo("Project detail");
        assertThat(response.description()).isEqualTo("Description");
        assertThat(response.content()).isEqualTo("Content");
        assertThat(response.locale()).isEqualTo("en");
        assertThat(response.translated()).isTrue();
    }

    @Test
    void getAllPagedShouldReturnPageResponse() {
        repository.pagedProjects = List.of(project(4L, "paged-project", "Web", false));
        repository.total = 7;

        PageResponse<ProjectResponse> response = service.getAllPaged(null, null, 1, 3);

        assertThat(response.content()).hasSize(1);
        assertThat(response.page()).isEqualTo(1);
        assertThat(response.totalElements()).isEqualTo(7);
    }

    @Test
    void getRelatedProjectsShouldReturnProjectsFromSameCategory() {
        Project current = project(5L, "current-project", "Web", true);
        repository.projects.add(current);
        repository.relatedProjects = List.of(project(6L, "related-project", "Web", false));

        List<ProjectResponse> responses = service.getRelatedProjects("current-project", 3);

        assertThat(responses).hasSize(1);
        assertThat(responses.getFirst().slug()).isEqualTo("related-project");
    }

    private Project project(Long id, String slug, String category, boolean featured) {
        return Project.builder()
                .id(id)
                .title(slug)
                .slug(slug)
                .category(category)
                .featured(featured)
                .titleSeo(slug + " seo")
                .descriptionSeo(slug + " description")
                .indexable(true)
                .build();
    }

    private static class InMemoryProjectRepository implements ProjectRepositoryPort {
        private final List<Project> projects = new ArrayList<>();
        private List<Project> categoryProjects = List.of();
        private List<Project> featuredProjects = List.of();
        private List<Project> pagedProjects = List.of();
        private List<Project> relatedProjects = List.of();
        private long total;

        @Override
        public Project save(Project project) {
            return project;
        }

        @Override
        public List<Project> findAll() {
            return List.copyOf(projects);
        }

        @Override
        public List<Project> findAllByCategory(String category) {
            return List.copyOf(categoryProjects);
        }

        @Override
        public List<Project> findFeatured() {
            return List.copyOf(featuredProjects);
        }

        @Override
        public List<Project> findAllPaged(Pageable pageable) {
            return List.copyOf(pagedProjects);
        }

        @Override
        public List<Project> findByCategoryPaged(String category, Pageable pageable) {
            return List.copyOf(categoryProjects);
        }

        @Override
        public List<Project> findFeaturedPaged(Pageable pageable) {
            return List.copyOf(featuredProjects);
        }

        @Override
        public long countAll() {
            return total;
        }

        @Override
        public long countByCategory(String category) {
            return categoryProjects.size();
        }

        @Override
        public long countFeatured() {
            return featuredProjects.size();
        }

        @Override
        public List<Project> findRelatedProjects(String category, Long excludeId, int limit) {
            return List.copyOf(relatedProjects);
        }

        @Override
        public Optional<Project> findById(Long id) {
            return projects.stream().filter(project -> id.equals(project.getId())).findFirst();
        }

        @Override
        public Optional<Project> findBySlug(String slug) {
            return projects.stream().filter(project -> slug.equals(project.getSlug())).findFirst();
        }

        @Override
        public void deleteById(Long id) {
        }

        @Override
        public boolean existsBySlug(String slug) {
            return false;
        }
    }

    private static class InMemoryProjectTranslationRepository implements ProjectTranslationRepositoryPort {
        private final List<ProjectTranslation> translations = new ArrayList<>();

        @Override
        public ProjectTranslation save(ProjectTranslation translation) {
            translations.removeIf(current ->
                    current.getProjectId().equals(translation.getProjectId()) &&
                            current.getLocale().equals(translation.getLocale()));
            translations.add(translation);
            return translation;
        }

        @Override
        public List<ProjectTranslation> saveAll(List<ProjectTranslation> translations) {
            translations.forEach(this::save);
            return translations;
        }

        @Override
        public Optional<ProjectTranslation> findByProjectIdAndLocale(Long projectId, String locale) {
            return translations.stream()
                    .filter(translation -> projectId.equals(translation.getProjectId()) && locale.equals(translation.getLocale()))
                    .findFirst();
        }

        @Override
        public List<ProjectTranslation> findByProjectIdInAndLocale(List<Long> projectIds, String locale) {
            return translations.stream()
                    .filter(translation -> projectIds.contains(translation.getProjectId()) && locale.equals(translation.getLocale()))
                    .toList();
        }

        @Override
        public void deleteByProjectId(Long projectId) {
            translations.removeIf(translation -> projectId.equals(translation.getProjectId()));
        }
    }
}
