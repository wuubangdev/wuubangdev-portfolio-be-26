package com.wuubangdev.portfolio.modules.user.infrastructure.adapter.input.rest;

import com.wuubangdev.portfolio.modules.user.application.dto.UserResponse;
import com.wuubangdev.portfolio.modules.user.application.service.UserService;
import com.wuubangdev.portfolio.modules.user.domain.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/users")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PatchMapping("/{id}/roles")
    public ResponseEntity<UserResponse> updateUserRoles(@PathVariable Long id, @RequestBody List<Role> roles) {
        return ResponseEntity.ok(userService.updateUserRoles(id, roles));
    }
}
