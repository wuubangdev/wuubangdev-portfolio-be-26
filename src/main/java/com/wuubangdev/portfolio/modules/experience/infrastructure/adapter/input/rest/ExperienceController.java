package com.wuubangdev.portfolio.modules.experience.infrastructure.adapter.input.rest;

import com.wuubangdev.portfolio.modules.experience.application.dto.ExperienceRequest;
import com.wuubangdev.portfolio.modules.experience.application.dto.ExperienceResponse;
import com.wuubangdev.portfolio.modules.experience.application.service.ExperienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ExperienceController {

    private final ExperienceService experienceService;

    @GetMapping("/api/v1/experiences")
    public ResponseEntity<List<ExperienceResponse>> getAll() {
        return ResponseEntity.ok(experienceService.getAll());
    }

    @PostMapping("/api/v1/admin/experiences")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ExperienceResponse> create(@RequestBody ExperienceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(experienceService.create(request));
    }

    @PutMapping("/api/v1/admin/experiences/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ExperienceResponse> update(@PathVariable Long id, @RequestBody ExperienceRequest request) {
        return ResponseEntity.ok(experienceService.update(id, request));
    }

    @DeleteMapping("/api/v1/admin/experiences/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        experienceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
