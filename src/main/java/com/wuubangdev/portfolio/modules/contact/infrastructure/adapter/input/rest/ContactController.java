package com.wuubangdev.portfolio.modules.contact.infrastructure.adapter.input.rest;

import com.wuubangdev.portfolio.modules.contact.application.dto.ContactRequest;
import com.wuubangdev.portfolio.modules.contact.application.dto.ContactResponse;
import com.wuubangdev.portfolio.modules.contact.application.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping("/api/v1/contact")
    public ResponseEntity<ContactResponse> submit(@Valid @RequestBody ContactRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contactService.submit(request));
    }

    @GetMapping("/api/v1/admin/contacts")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ContactResponse>> getAll() {
        return ResponseEntity.ok(contactService.getAll());
    }

    @PatchMapping("/api/v1/admin/contacts/{id}/read")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ContactResponse> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(contactService.markAsRead(id));
    }
}
