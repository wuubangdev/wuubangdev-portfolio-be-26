package com.wuubangdev.portfolio.modules.project.infrastructure.adapter.input.rest;

import com.wuubangdev.portfolio.infrastructure.global.api.PageResponse;
import com.wuubangdev.portfolio.modules.project.application.dto.ProjectRequest;
import com.wuubangdev.portfolio.modules.project.application.dto.ProjectResponse;
import com.wuubangdev.portfolio.modules.project.application.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/api/v1/projects")
    public ResponseEntity<List<ProjectResponse>> getAll(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean featured
    ) {
        return ResponseEntity.ok(projectService.getAll(category, featured));
    }

    @GetMapping("/api/v1/projects/paged")
    public ResponseEntity<PageResponse<ProjectResponse>> getAllPaged(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean featured,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(projectService.getAllPaged(category, featured, page, size));
    }

    @GetMapping("/api/v1/projects/featured")
    public ResponseEntity<List<ProjectResponse>> getFeatured() {
        return ResponseEntity.ok(projectService.getFeatured());
    }

    @GetMapping("/api/v1/projects/{slug}")
    public ResponseEntity<ProjectResponse> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(projectService.getBySlug(slug));
    }

    @GetMapping("/api/v1/projects/{slug}/related")
    public ResponseEntity<List<ProjectResponse>> getRelatedProjects(
            @PathVariable String slug,
            @RequestParam(defaultValue = "3") int limit
    ) {
        return ResponseEntity.ok(projectService.getRelatedProjects(slug, limit));
    }

    @GetMapping("/api/v1/admin/projects")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProjectResponse>> getAllAdmin() {
        return ResponseEntity.ok(projectService.getAll(null, null));
    }

    @GetMapping("/api/v1/admin/projects/paged")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<ProjectResponse>> getAllAdminPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(projectService.getAllPaged(null, null, page, size));
    }

    @GetMapping("/api/v1/admin/projects/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProjectResponse> getByIdAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getById(id));
    }

    @PostMapping("/api/v1/admin/projects")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProjectResponse> create(@Valid @RequestBody ProjectRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.create(request));
    }

    @PutMapping("/api/v1/admin/projects/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProjectResponse> update(@PathVariable Long id, @Valid @RequestBody ProjectRequest request) {
        return ResponseEntity.ok(projectService.update(id, request));
    }

    @DeleteMapping("/api/v1/admin/projects/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
