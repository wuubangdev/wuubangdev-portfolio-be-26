package com.wuubangdev.portfolio.modules.education.infrastructure.adapter.input.rest;

import com.wuubangdev.portfolio.modules.education.application.dto.EducationRequest;
import com.wuubangdev.portfolio.modules.education.application.dto.EducationResponse;
import com.wuubangdev.portfolio.modules.education.application.service.EducationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/educations")
@RequiredArgsConstructor
public class EducationController {

    private final EducationService educationService;

    @GetMapping
    public ResponseEntity<List<EducationResponse>> getAllEducations() {
        return ResponseEntity.ok(educationService.getAllEducations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EducationResponse> getEducationById(@PathVariable Long id) {
        return ResponseEntity.ok(educationService.getEducationById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EducationResponse> createEducation(@RequestBody EducationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(educationService.createEducation(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EducationResponse> updateEducation(@PathVariable Long id, @RequestBody EducationRequest request) {
        return ResponseEntity.ok(educationService.updateEducation(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEducation(@PathVariable Long id) {
        educationService.deleteEducation(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/order")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EducationResponse> setOrder(@PathVariable Long id, @RequestParam Integer order) {
        return ResponseEntity.ok(educationService.setOrder(id, order));
    }

    @PutMapping("/{id}/public")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EducationResponse> togglePublic(@PathVariable Long id, @RequestParam Boolean isPublic) {
        return ResponseEntity.ok(educationService.togglePublic(id, isPublic));
    }
}
