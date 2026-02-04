package com.wuubangdev.portfolio.modules.user.infrastructure.adapter.input.rest;

import com.wuubangdev.portfolio.modules.user.application.dto.LoginRequest;
import com.wuubangdev.portfolio.modules.user.application.dto.LoginResponse;
import com.wuubangdev.portfolio.modules.user.application.dto.RegisterRequest;
import com.wuubangdev.portfolio.modules.user.application.dto.UserResponse;
import com.wuubangdev.portfolio.modules.user.application.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()") // Chỉ những người đã đăng nhập (có Token) mới gọi được
    public ResponseEntity<UserResponse> getMe() {
        // Lấy thông tin username từ Token trong SecurityContext
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        UserResponse response = authService.getProfile(username);
        return ResponseEntity.ok(response);
    }
}