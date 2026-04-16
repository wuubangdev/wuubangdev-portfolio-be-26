package com.wuubangdev.portfolio.modules.user.application.service;

import com.wuubangdev.portfolio.infrastructure.global.exception.BusinessException;
import com.wuubangdev.portfolio.infrastructure.global.exception.ResourceNotFoundException;
import com.wuubangdev.portfolio.infrastructure.global.security.jwt.JwtTokenProvider;
import com.wuubangdev.portfolio.modules.user.application.dto.*;
import com.wuubangdev.portfolio.modules.user.domain.model.Role;
import com.wuubangdev.portfolio.modules.user.domain.model.User;
import com.wuubangdev.portfolio.modules.user.domain.port.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final MessageSource messageSource;

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        if (userRepositoryPort.findByUsername(request.username()).isPresent()) {
            throw new BusinessException(getMessage("auth.register.failed", new Object[]{request.username()}));
        }

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .roles(List.of(Role.ROLE_USER))
                .build();

        userRepositoryPort.save(user);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            String token = jwtTokenProvider.generateToken(authentication.getName());
            return new LoginResponse(token, "Bearer", getMessage("auth.login.success"));
        } catch (Exception e) {
            throw new BusinessException(getMessage("auth.login.failed"));
        }
    }

    @Override
    public UserResponse getProfile(String username) {
        User user = userRepositoryPort.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", username));
        return new UserResponse(user.getUsername(), user.getEmail(), user.getRoles());
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