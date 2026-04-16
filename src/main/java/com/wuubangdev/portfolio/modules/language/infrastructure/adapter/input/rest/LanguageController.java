package com.wuubangdev.portfolio.modules.language.infrastructure.adapter.input.rest;

import com.wuubangdev.portfolio.modules.language.application.dto.LanguageRequest;
import com.wuubangdev.portfolio.modules.language.application.dto.LanguageResponse;
import com.wuubangdev.portfolio.modules.language.application.service.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/languages")
@RequiredArgsConstructor
public class LanguageController {

    private final LanguageService languageService;

    @GetMapping
    public ResponseEntity<List<LanguageResponse>> getAllLanguages() {
        return ResponseEntity.ok(languageService.getAllLanguages());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LanguageResponse> getLanguageById(@PathVariable Long id) {
        return ResponseEntity.ok(languageService.getLanguageById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LanguageResponse> createLanguage(@RequestBody LanguageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(languageService.createLanguage(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LanguageResponse> updateLanguage(@PathVariable Long id, @RequestBody LanguageRequest request) {
        return ResponseEntity.ok(languageService.updateLanguage(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteLanguage(@PathVariable Long id) {
        languageService.deleteLanguage(id);
        return ResponseEntity.noContent().build();
    }
}
