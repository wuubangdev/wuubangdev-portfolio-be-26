package com.wuubangdev.portfolio.modules.user.application.service;

import com.wuubangdev.portfolio.infrastructure.global.config.JwtProperties;
import com.wuubangdev.portfolio.infrastructure.global.exception.BusinessException;
import com.wuubangdev.portfolio.infrastructure.global.mail.MailService;
import com.wuubangdev.portfolio.infrastructure.global.security.jwt.JwtTokenProvider;
import com.wuubangdev.portfolio.modules.user.application.dto.ForgotPasswordRequest;
import com.wuubangdev.portfolio.modules.user.application.dto.LoginResponse;
import com.wuubangdev.portfolio.modules.user.application.dto.RefreshTokenRequest;
import com.wuubangdev.portfolio.modules.user.application.dto.RegisterRequest;
import com.wuubangdev.portfolio.modules.user.application.dto.ResetPasswordRequest;
import com.wuubangdev.portfolio.modules.user.application.dto.SocialLoginRequest;
import com.wuubangdev.portfolio.modules.user.application.dto.SocialUserProfile;
import com.wuubangdev.portfolio.modules.user.application.port.SocialAuthClient;
import com.wuubangdev.portfolio.modules.user.domain.model.Role;
import com.wuubangdev.portfolio.modules.user.domain.model.User;
import com.wuubangdev.portfolio.modules.user.domain.model.UserType;
import com.wuubangdev.portfolio.modules.user.domain.port.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthServiceImplTest {

    private InMemoryUserRepository userRepository;
    private CapturingMailSender mailSender;
    private PasswordEncoder passwordEncoder;
    private AuthServiceImpl authService;
    private FakeSocialAuthClient googleClient;
    private FakeSocialAuthClient githubClient;
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        mailSender = new CapturingMailSender();
        passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        googleClient = new FakeSocialAuthClient();
        githubClient = new FakeSocialAuthClient();

        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setSecret("super-secret-key-super-secret-key-123456");
        jwtProperties.setExpirationMs(86400000L);
        jwtProperties.setRefreshExpirationMs(2592000000L);

        jwtTokenProvider = new JwtTokenProvider(jwtProperties);

        authService = new AuthServiceImpl(
                userRepository,
                passwordEncoder,
                jwtTokenProvider,
                authentication -> authentication,
                messageSource(),
                new MailService(mailSender),
                googleClient,
                githubClient
        );

        ReflectionTestUtils.setField(authService, "activationUrlBase", "http://localhost:3000/activate-account");
        ReflectionTestUtils.setField(authService, "resetPasswordUrlBase", "http://localhost:3000/reset-password");
        ReflectionTestUtils.setField(authService, "activationTokenExpirationHours", 24);
        ReflectionTestUtils.setField(authService, "resetTokenExpirationMinutes", 30);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        LocaleContextHolder.resetLocaleContext();
    }

    @Test
    void registerShouldCreateInactiveUserAndSendActivationMail() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);

        authService.register(new RegisterRequest("alice", "alice@example.com", "secret123"));

        User savedUser = userRepository.savedUser;
        assertThat(savedUser.getUsername()).isEqualTo("alice");
        assertThat(savedUser.getEmail()).isEqualTo("alice@example.com");
        assertThat(savedUser.getRoles()).containsExactly(Role.ROLE_USER);
        assertThat(savedUser.getEnabled()).isFalse();
        assertThat(savedUser.getActivationToken()).isNotBlank();
        assertThat(mailSender.lastMessage).isNotNull();
        assertThat(mailSender.lastMessage.getTo()).containsExactly("alice@example.com");
        assertThat(mailSender.lastMessage.getSubject()).isEqualTo("Activate your account, alice");
        assertThat(mailSender.lastMessage.getText()).contains("http://localhost:3000/activate-account?token=");
    }

    @Test
    void registerShouldSendVietnameseActivationMailWhenLocaleIsVi() {
        LocaleContextHolder.setLocale(Locale.forLanguageTag("vi"));

        authService.register(new RegisterRequest("linh", "linh@example.com", "secret123"));

        assertThat(mailSender.lastMessage).isNotNull();
        assertThat(mailSender.lastMessage.getSubject()).isEqualTo("Kich hoat tai khoan cua ban, linh");
        assertThat(mailSender.lastMessage.getText()).contains("Vui long kich hoat tai khoan");
    }

    @Test
    void activateAccountShouldEnableUserAndClearToken() {
        User user = baseUser();
        user.setEnabled(false);
        user.setActivationToken("activate-token");
        user.setActivationTokenExpiresAt(LocalDateTime.now().plusHours(1));
        userRepository.users.add(user);

        authService.activateAccount("activate-token");

        assertThat(userRepository.savedUser.getEnabled()).isTrue();
        assertThat(userRepository.savedUser.getActivationToken()).isNull();
        assertThat(userRepository.savedUser.getActivationTokenExpiresAt()).isNull();
    }

    @Test
    void forgotPasswordShouldSaveResetTokenAndSendMail() {
        User user = baseUser();
        userRepository.users.add(user);

        authService.forgotPassword(new ForgotPasswordRequest("john@example.com"));

        assertThat(userRepository.savedUser.getResetPasswordToken()).isNotBlank();
        assertThat(userRepository.savedUser.getResetPasswordTokenExpiresAt()).isAfter(LocalDateTime.now().minusMinutes(1));
        assertThat(mailSender.lastMessage).isNotNull();
        assertThat(mailSender.lastMessage.getText()).contains("http://localhost:3000/reset-password?token=");
    }

    @Test
    void resetPasswordShouldUpdatePasswordAndClearResetToken() {
        User user = baseUser();
        user.setResetPasswordToken("reset-token");
        user.setResetPasswordTokenExpiresAt(LocalDateTime.now().plusMinutes(30));
        userRepository.users.add(user);

        authService.resetPassword(new ResetPasswordRequest("reset-token", "new-secret"));

        assertThat(passwordEncoder.matches("new-secret", userRepository.savedUser.getPassword())).isTrue();
        assertThat(userRepository.savedUser.getResetPasswordToken()).isNull();
        assertThat(userRepository.savedUser.getResetPasswordTokenExpiresAt()).isNull();
    }

    @Test
    void resetPasswordShouldRejectExpiredToken() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);

        User user = baseUser();
        user.setResetPasswordToken("expired-token");
        user.setResetPasswordTokenExpiresAt(LocalDateTime.now().minusMinutes(1));
        userRepository.users.add(user);

        assertThatThrownBy(() -> authService.resetPassword(new ResetPasswordRequest("expired-token", "new-secret")))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Reset password token has expired");
    }

    @Test
    void refreshTokenShouldIssueNewTokens() {
        User user = baseUser();
        String previousRefreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());
        user.setRefreshToken(previousRefreshToken);
        user.setRefreshTokenExpiresAt(LocalDateTime.now().plusDays(1));
        userRepository.users.add(user);

        LoginResponse response = authService.refreshToken(new RefreshTokenRequest(user.getRefreshToken()));

        assertThat(response.accessToken()).isNotBlank();
        assertThat(response.refreshToken()).isNotBlank();
        assertThat(response.refreshToken()).isNotEqualTo(previousRefreshToken);
        assertThat(userRepository.savedUser.getRefreshToken()).isEqualTo(response.refreshToken());
    }

    @Test
    void loginWithGoogleShouldCreateOrUpdateUserAndIssueTokens() {
        googleClient.profile = new SocialUserProfile(
                "GOOGLE",
                "google-123",
                "social@example.com",
                "Social User",
                "https://example.com/avatar.png"
        );

        LoginResponse response = authService.loginWithGoogle(new SocialLoginRequest("google-access-token"));

        assertThat(response.accessToken()).isNotBlank();
        assertThat(response.refreshToken()).isNotBlank();
        assertThat(userRepository.savedUser.getEmail()).isEqualTo("social@example.com");
        assertThat(userRepository.savedUser.getGoogleId()).isEqualTo("google-123");
        assertThat(userRepository.savedUser.getUserType()).isEqualTo(UserType.GOOGLE);
        assertThat(userRepository.savedUser.getEnabled()).isTrue();
    }

    private StaticMessageSource messageSource() {
        StaticMessageSource source = new StaticMessageSource();
        source.addMessage("auth.activation.mail.subject", Locale.ENGLISH, "Activate your account, {0}");
        source.addMessage("auth.activation.mail.body", Locale.ENGLISH, "Hi {0},\n\nPlease activate your account by clicking the link below:\n{1}");
        Locale vi = Locale.forLanguageTag("vi");
        source.addMessage("auth.activation.mail.subject", vi, "Kich hoat tai khoan cua ban, {0}");
        source.addMessage("auth.activation.mail.body", vi, "Chao {0},\n\nVui long kich hoat tai khoan bang cach bam vao lien ket ben duoi:\n{1}");
        source.addMessage("auth.register.failed", Locale.ENGLISH, "Registration failed");
        source.addMessage("auth.email.exists", Locale.ENGLISH, "Email already exists");
        source.addMessage("auth.account.not.activated", Locale.ENGLISH, "Account is not activated yet");
        source.addMessage("auth.activation.invalid", Locale.ENGLISH, "Invalid activation token");
        source.addMessage("auth.activation.expired", Locale.ENGLISH, "Activation token has expired");
        source.addMessage("auth.reset.invalid", Locale.ENGLISH, "Invalid reset password token");
        source.addMessage("auth.reset.expired", Locale.ENGLISH, "Reset password token has expired");
        source.addMessage("auth.login.failed", Locale.ENGLISH, "Invalid username or password");
        source.addMessage("auth.login.success", Locale.ENGLISH, "Login successful");
        source.addMessage("auth.refresh.success", Locale.ENGLISH, "Token refreshed successfully");
        source.addMessage("auth.refresh.invalid", Locale.ENGLISH, "Invalid refresh token");
        source.addMessage("auth.refresh.expired", Locale.ENGLISH, "Refresh token has expired");
        source.addMessage("auth.social.login.success", Locale.ENGLISH, "{0} login successful");
        source.addMessage("auth.social.email.required", Locale.ENGLISH, "Social account must provide an email address");
        source.addMessage("auth.register.failed", vi, "Dang ky that bai");
        source.addMessage("auth.email.exists", vi, "Email da ton tai");
        source.addMessage("auth.account.not.activated", vi, "Tai khoan chua duoc kich hoat");
        source.addMessage("auth.activation.invalid", vi, "Token kich hoat khong hop le");
        source.addMessage("auth.activation.expired", vi, "Token kich hoat da het han");
        source.addMessage("auth.reset.invalid", vi, "Token dat lai mat khau khong hop le");
        source.addMessage("auth.reset.expired", vi, "Token dat lai mat khau da het han");
        source.addMessage("auth.login.failed", vi, "Ten dang nhap hoac mat khau khong dung");
        source.addMessage("auth.login.success", vi, "Dang nhap thanh cong");
        source.addMessage("auth.refresh.success", vi, "Lam moi token thanh cong");
        source.addMessage("auth.refresh.invalid", vi, "Refresh token khong hop le");
        source.addMessage("auth.refresh.expired", vi, "Refresh token da het han");
        source.addMessage("auth.social.login.success", vi, "Dang nhap {0} thanh cong");
        source.addMessage("auth.social.email.required", vi, "Tai khoan social bat buoc phai co email");
        return source;
    }

    private User baseUser() {
        return User.builder()
                .id(1L)
                .username("john")
                .email("john@example.com")
                .password(passwordEncoder.encode("secret123"))
                .roles(List.of(Role.ROLE_USER))
                .enabled(true)
                .userType(UserType.BASIC)
                .build();
    }

    private static class InMemoryUserRepository implements UserRepositoryPort {
        private final List<User> users = new ArrayList<>();
        private User savedUser;

        @Override
        public Optional<User> findByUsername(String username) {
            return users.stream().filter(user -> username.equals(user.getUsername())).findFirst();
        }

        @Override
        public Optional<User> findByEmail(String email) {
            return users.stream().filter(user -> email.equals(user.getEmail())).findFirst();
        }

        @Override
        public Optional<User> findByActivationToken(String activationToken) {
            return users.stream().filter(user -> activationToken.equals(user.getActivationToken())).findFirst();
        }

        @Override
        public Optional<User> findByResetPasswordToken(String resetPasswordToken) {
            return users.stream().filter(user -> resetPasswordToken.equals(user.getResetPasswordToken())).findFirst();
        }

        @Override
        public Optional<User> findByRefreshToken(String refreshToken) {
            return users.stream().filter(user -> refreshToken.equals(user.getRefreshToken())).findFirst();
        }

        @Override
        public Optional<User> findById(Long id) {
            return users.stream().filter(user -> id.equals(user.getId())).findFirst();
        }

        @Override
        public List<User> findAll() {
            return List.copyOf(users);
        }

        @Override
        public User save(User user) {
            if (user.getId() == null) {
                user.setId((long) (users.size() + 1));
            }
            users.removeIf(existing -> existing.getId().equals(user.getId()));
            users.add(user);
            savedUser = user;
            return user;
        }

        @Override
        public boolean existsByEmail(String email) {
            return users.stream().anyMatch(user -> email.equals(user.getEmail()));
        }
    }

    private static class FakeSocialAuthClient implements SocialAuthClient {
        private SocialUserProfile profile;

        @Override
        public SocialUserProfile fetchUserProfile(String accessToken) {
            return profile;
        }
    }

    private static class CapturingMailSender implements JavaMailSender {
        private SimpleMailMessage lastMessage;

        @Override
        public jakarta.mail.internet.MimeMessage createMimeMessage(InputStream contentStream) {
            throw new UnsupportedOperationException();
        }

        @Override
        public jakarta.mail.internet.MimeMessage createMimeMessage() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void send(jakarta.mail.internet.MimeMessage mimeMessage) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void send(jakarta.mail.internet.MimeMessage... mimeMessages) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void send(org.springframework.mail.javamail.MimeMessagePreparator mimeMessagePreparator) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void send(org.springframework.mail.javamail.MimeMessagePreparator... mimeMessagePreparators) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void send(SimpleMailMessage simpleMessage) {
            this.lastMessage = simpleMessage;
        }

        @Override
        public void send(SimpleMailMessage... simpleMessages) {
            if (simpleMessages.length > 0) {
                this.lastMessage = simpleMessages[simpleMessages.length - 1];
            }
        }
    }
}
