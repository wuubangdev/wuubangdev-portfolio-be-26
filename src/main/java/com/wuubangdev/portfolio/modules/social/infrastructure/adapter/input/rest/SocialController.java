package com.wuubangdev.portfolio.modules.social.infrastructure.adapter.input.rest;

import com.wuubangdev.portfolio.modules.social.application.dto.SocialRequest;
import com.wuubangdev.portfolio.modules.social.application.dto.SocialResponse;
import com.wuubangdev.portfolio.modules.social.application.service.SocialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SocialController {

    private final SocialService socialService;

    @GetMapping("/api/v1/social")
    public ResponseEntity<SocialResponse> getSocial() {
        return ResponseEntity.ok(socialService.getSocial());
    }

    @PutMapping("/api/v1/admin/social")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SocialResponse> updateSocial(@RequestBody SocialRequest request) {
        return ResponseEntity.ok(socialService.upsertSocial(request));
    }
}
