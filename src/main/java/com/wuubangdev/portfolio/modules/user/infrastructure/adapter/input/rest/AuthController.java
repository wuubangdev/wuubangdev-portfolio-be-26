package com.wuubangdev.portfolio.modules.user.infrastructure.adapter.input.rest;

import com.wuubangdev.portfolio.modules.user.application.dto.LoginRequest;
import com.wuubangdev.portfolio.modules.user.application.dto.LoginResponse;
import com.wuubangdev.portfolio.modules.user.application.dto.RegisterRequest;
import com.wuubangdev.portfolio.modules.user.application.dto.ForgotPasswordRequest;
import com.wuubangdev.portfolio.modules.user.application.dto.RefreshTokenRequest;
import com.wuubangdev.portfolio.modules.user.application.dto.ResetPasswordRequest;
import com.wuubangdev.portfolio.modules.user.application.dto.SocialLoginRequest;
import com.wuubangdev.portfolio.modules.user.application.dto.UserResponse;
import com.wuubangdev.portfolio.modules.user.application.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Locale;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final MessageSource messageSource;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(getMessage("auth.register.success"));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping("/social/google")
    public ResponseEntity<LoginResponse> loginWithGoogle(@Valid @RequestBody SocialLoginRequest request) {
        return ResponseEntity.ok(authService.loginWithGoogle(request));
    }

    @PostMapping("/social/github")
    public ResponseEntity<LoginResponse> loginWithGithub(@Valid @RequestBody SocialLoginRequest request) {
        return ResponseEntity.ok(authService.loginWithGithub(request));
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateAccount(@RequestParam String token) {
        authService.activateAccount(token);
        return ResponseEntity.ok(getMessage("auth.activation.success"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ResponseEntity.ok(getMessage("auth.forgot.password.sent"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(getMessage("auth.reset.success"));
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()") // Chỉ những người đã đăng nhập (có Token) mới gọi được
    public ResponseEntity<UserResponse> getMe() {
        // Lấy thông tin username từ Token trong SecurityContext
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        UserResponse response = authService.getProfile(username);
        return ResponseEntity.ok(response);
    }

    private String getMessage(String key, Object... args) {
        Locale locale = getCurrentLocale();
        try {
            return messageSource.getMessage(key, args, locale);
        } catch (Exception e) {
            return key; // Fallback to key if message not found
        }
    }

    private Locale getCurrentLocale() {
        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            return (Locale) attrs.getRequest().getSession().getAttribute("org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE");
        } catch (Exception e) {
            return Locale.getDefault();
        }
    }
}
