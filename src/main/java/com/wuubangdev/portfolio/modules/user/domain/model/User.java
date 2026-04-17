package com.wuubangdev.portfolio.modules.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String username;
    private String password;
    private String email;
    private List<Role> roles;
    private Boolean enabled;
    private UserType userType;
    private String googleId;
    private String githubId;
    private String activationToken;
    private LocalDateTime activationTokenExpiresAt;
    private String resetPasswordToken;
    private LocalDateTime resetPasswordTokenExpiresAt;
    private String refreshToken;
    private LocalDateTime refreshTokenExpiresAt;
}
