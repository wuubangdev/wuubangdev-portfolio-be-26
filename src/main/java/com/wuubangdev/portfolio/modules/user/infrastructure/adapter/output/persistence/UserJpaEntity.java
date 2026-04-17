package com.wuubangdev.portfolio.modules.user.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.infrastructure.persistence.base.BaseEntity;
import com.wuubangdev.portfolio.modules.user.domain.model.Role;
import com.wuubangdev.portfolio.modules.user.domain.model.UserType;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserJpaEntity extends BaseEntity {
    @Indexed(unique = true)
    private String username;

    private String password;

    @Indexed(unique = true)
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
