package com.wuubangdev.portfolio.modules.user.application.service;

import com.wuubangdev.portfolio.infrastructure.global.exception.BusinessException;
import com.wuubangdev.portfolio.infrastructure.global.exception.ResourceNotFoundException;
import com.wuubangdev.portfolio.infrastructure.global.mail.MailService;
import com.wuubangdev.portfolio.infrastructure.global.security.jwt.JwtTokenProvider;
import com.wuubangdev.portfolio.modules.user.application.dto.*;
import com.wuubangdev.portfolio.modules.user.application.port.SocialAuthClient;
import com.wuubangdev.portfolio.modules.user.domain.model.Role;
import com.wuubangdev.portfolio.modules.user.domain.model.User;
import com.wuubangdev.portfolio.modules.user.domain.model.UserType;
import com.wuubangdev.portfolio.modules.user.domain.port.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final int DEFAULT_ACTIVATION_TOKEN_HOURS = 24;
    private static final int DEFAULT_RESET_TOKEN_MINUTES = 30;

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final MessageSource messageSource;
    private final MailService mailService;
    @Qualifier("googleSocialAuthClient")
    private final SocialAuthClient googleSocialAuthClient;
    @Qualifier("githubSocialAuthClient")
    private final SocialAuthClient githubSocialAuthClient;

    @Value("${app.auth.activation-url-base:http://localhost:3000/activate-account}")
    private String activationUrlBase;

    @Value("${app.auth.reset-password-url-base:http://localhost:3000/reset-password}")
    private String resetPasswordUrlBase;

    @Value("${app.auth.activation-token-expiration-hours:24}")
    private int activationTokenExpirationHours;

    @Value("${app.auth.reset-token-expiration-minutes:30}")
    private int resetTokenExpirationMinutes;

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        if (userRepositoryPort.findByUsername(request.username()).isPresent()) {
            throw new BusinessException(getMessage("auth.register.failed", new Object[]{request.username()}));
        }
        if (userRepositoryPort.existsByEmail(request.email())) {
            throw new BusinessException(getMessage("auth.email.exists", request.email()));
        }

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .roles(List.of(Role.ROLE_USER))
                .enabled(false)
                .userType(UserType.BASIC)
                .activationToken(generateToken())
                .activationTokenExpiresAt(LocalDateTime.now().plusHours(getActivationTokenExpirationHours()))
                .build();

        User savedUser = userRepositoryPort.save(user);
        mailService.sendAccountActivationEmail(
                savedUser.getEmail(),
                getMessage("auth.activation.mail.subject", savedUser.getUsername()),
                getMessage("auth.activation.mail.body", savedUser.getUsername(), buildActivationLink(savedUser.getActivationToken()))
        );
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            User user = userRepositoryPort.findByUsername(authentication.getName())
                    .orElseThrow(() -> new ResourceNotFoundException("User", authentication.getName()));
            return issueTokens(user, getMessage("auth.login.success"));
        } catch (DisabledException e) {
            throw new BusinessException(getMessage("auth.account.not.activated"));
        } catch (Exception e) {
            throw new BusinessException(getMessage("auth.login.failed"));
        }
    }

    @Override
    @Transactional
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        if (!jwtTokenProvider.validateRefreshToken(request.refreshToken())) {
            throw new BusinessException(getMessage("auth.refresh.invalid"));
        }

        User user = userRepositoryPort.findByRefreshToken(request.refreshToken())
                .orElseThrow(() -> new BusinessException(getMessage("auth.refresh.invalid")));

        if (isExpired(user.getRefreshTokenExpiresAt())) {
            throw new BusinessException(getMessage("auth.refresh.expired"));
        }

        return issueTokens(user, getMessage("auth.refresh.success"));
    }

    @Override
    @Transactional
    public LoginResponse loginWithGoogle(SocialLoginRequest request) {
        SocialUserProfile profile = googleSocialAuthClient.fetchUserProfile(request.accessToken());
        User user = upsertSocialUser(profile, "GOOGLE");
        return issueTokens(user, getMessage("auth.social.login.success", "Google"));
    }

    @Override
    @Transactional
    public LoginResponse loginWithGithub(SocialLoginRequest request) {
        SocialUserProfile profile = githubSocialAuthClient.fetchUserProfile(request.accessToken());
        User user = upsertSocialUser(profile, "GITHUB");
        return issueTokens(user, getMessage("auth.social.login.success", "GitHub"));
    }

    @Override
    public UserResponse getProfile(String username) {
        User user = userRepositoryPort.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", username));
        return new UserResponse(user.getUsername(), user.getEmail(), user.getRoles(), isEnabled(user), user.getUserType());
    }

    @Override
    @Transactional
    public void activateAccount(String token) {
        User user = userRepositoryPort.findByActivationToken(token)
                .orElseThrow(() -> new BusinessException(getMessage("auth.activation.invalid")));
        if (isExpired(user.getActivationTokenExpiresAt())) {
            throw new BusinessException(getMessage("auth.activation.expired"));
        }
        user.setEnabled(true);
        user.setActivationToken(null);
        user.setActivationTokenExpiresAt(null);
        userRepositoryPort.save(user);
    }

    @Override
    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        userRepositoryPort.findByEmail(request.email()).ifPresent(user -> {
            user.setResetPasswordToken(generateToken());
            user.setResetPasswordTokenExpiresAt(LocalDateTime.now().plusMinutes(getResetTokenExpirationMinutes()));
            userRepositoryPort.save(user);
            mailService.sendResetPasswordEmail(
                    user.getEmail(),
                    user.getUsername(),
                    buildResetPasswordLink(user.getResetPasswordToken())
            );
        });
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepositoryPort.findByResetPasswordToken(request.token())
                .orElseThrow(() -> new BusinessException(getMessage("auth.reset.invalid")));
        if (isExpired(user.getResetPasswordTokenExpiresAt())) {
            throw new BusinessException(getMessage("auth.reset.expired"));
        }
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        user.setResetPasswordToken(null);
        user.setResetPasswordTokenExpiresAt(null);
        userRepositoryPort.save(user);
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
        return LocaleContextHolder.getLocale();
    }

    private boolean isEnabled(User user) {
        return !Boolean.FALSE.equals(user.getEnabled());
    }

    private boolean isExpired(LocalDateTime expiresAt) {
        return expiresAt == null || expiresAt.isBefore(LocalDateTime.now());
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    private String buildActivationLink(String token) {
        return activationUrlBase + "?token=" + token;
    }

    private String buildResetPasswordLink(String token) {
        return resetPasswordUrlBase + "?token=" + token;
    }

    private int getActivationTokenExpirationHours() {
        return activationTokenExpirationHours > 0 ? activationTokenExpirationHours : DEFAULT_ACTIVATION_TOKEN_HOURS;
    }

    private int getResetTokenExpirationMinutes() {
        return resetTokenExpirationMinutes > 0 ? resetTokenExpirationMinutes : DEFAULT_RESET_TOKEN_MINUTES;
    }

    private LoginResponse issueTokens(User user, String message) {
        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

        user.setRefreshToken(refreshToken);
        user.setRefreshTokenExpiresAt(LocalDateTime.now().plusSeconds(jwtTokenProvider.getRefreshTokenExpirationMs() / 1000));
        userRepositoryPort.save(user);

        return new LoginResponse(
                accessToken,
                refreshToken,
                "Bearer",
                jwtTokenProvider.getAccessTokenExpirationMs() / 1000,
                jwtTokenProvider.getRefreshTokenExpirationMs() / 1000,
                message
        );
    }

    private User upsertSocialUser(SocialUserProfile profile, String provider) {
        if (profile.email() == null || profile.email().isBlank()) {
            throw new BusinessException(getMessage("auth.social.email.required"));
        }

        User user = userRepositoryPort.findByEmail(profile.email())
                .orElseGet(() -> User.builder()
                        .username(generateUsername(profile))
                        .email(profile.email())
                        .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                        .roles(List.of(Role.ROLE_USER))
                        .build());

        user.setEnabled(true);
        user.setUserType(resolveUserType(provider));
        user.setActivationToken(null);
        user.setActivationTokenExpiresAt(null);

        if ("GOOGLE".equals(provider)) {
            user.setGoogleId(profile.providerId());
        }
        if ("GITHUB".equals(provider)) {
            user.setGithubId(profile.providerId());
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        }
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(List.of(Role.ROLE_USER));
        }

        return userRepositoryPort.save(user);
    }

    private UserType resolveUserType(String provider) {
        return switch (provider) {
            case "GOOGLE" -> UserType.GOOGLE;
            case "GITHUB" -> UserType.GITHUB;
            default -> UserType.BASIC;
        };
    }

    private String generateUsername(SocialUserProfile profile) {
        String base = (profile.name() != null && !profile.name().isBlank() ? profile.name() : profile.email().split("@")[0])
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");

        if (base.isBlank()) {
            base = "user";
        }

        String candidate = base;
        int counter = 1;
        while (userRepositoryPort.findByUsername(candidate).isPresent()) {
            candidate = base + "-" + counter++;
        }
        return candidate;
    }
}
