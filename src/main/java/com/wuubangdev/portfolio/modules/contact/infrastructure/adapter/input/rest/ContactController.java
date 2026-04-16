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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ContactResponse>> getAll() {
        return ResponseEntity.ok(contactService.getAll());
    }

    @PatchMapping("/api/v1/admin/contacts/{id}/read")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ContactResponse> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(contactService.markAsRead(id));
    }

    @PutMapping("/api/v1/admin/contacts/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ContactResponse> updateContact(@PathVariable Long id, @RequestBody ContactRequest request) {
        return ResponseEntity.ok(contactService.update(id, request));
    }

    @PatchMapping("/api/v1/admin/contacts/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ContactResponse> changeStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(contactService.changeStatus(id, status));
    }
}
