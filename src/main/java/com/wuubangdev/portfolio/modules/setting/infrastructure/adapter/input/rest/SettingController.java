package com.wuubangdev.portfolio.modules.setting.infrastructure.adapter.input.rest;

import com.wuubangdev.portfolio.modules.setting.application.dto.SettingRequest;
import com.wuubangdev.portfolio.modules.setting.application.dto.SettingResponse;
import com.wuubangdev.portfolio.modules.setting.application.service.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SettingController {

    private final SettingService settingService;

    @GetMapping("/api/v1/setting")
    public ResponseEntity<SettingResponse> getSetting() {
        return ResponseEntity.ok(settingService.getSetting());
    }

    @PutMapping("/api/v1/admin/setting")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SettingResponse> updateSetting(@RequestBody SettingRequest request) {
        return ResponseEntity.ok(settingService.upsertSetting(request));
    }
}
