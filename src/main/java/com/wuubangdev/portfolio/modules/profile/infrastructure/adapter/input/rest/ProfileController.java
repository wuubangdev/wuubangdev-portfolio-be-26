package com.wuubangdev.portfolio.modules.profile.infrastructure.adapter.input.rest;

import com.wuubangdev.portfolio.modules.profile.application.dto.ProfileRequest;
import com.wuubangdev.portfolio.modules.profile.application.dto.ProfileResponse;
import com.wuubangdev.portfolio.modules.profile.application.dto.ProfileTranslationRequest;
import com.wuubangdev.portfolio.modules.profile.application.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/api/v1/profile")
    public ResponseEntity<ProfileResponse> getProfile() {
        return ResponseEntity.ok(profileService.getProfile());
    }

    @PutMapping("/api/v1/admin/profile")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProfileResponse> updateProfile(@RequestBody ProfileRequest request) {
        return ResponseEntity.ok(profileService.upsertProfile(request));
    }

    @PutMapping("/api/v1/admin/profile/translations/{locale}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProfileResponse> upsertTranslation(@PathVariable String locale, @RequestBody ProfileTranslationRequest request) {
        return ResponseEntity.ok(profileService.upsertTranslation(locale, request));
    }
}
