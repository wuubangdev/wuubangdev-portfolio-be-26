package com.wuubangdev.portfolio.modules.user.application.dto;

import com.wuubangdev.portfolio.modules.user.domain.model.Role;
import com.wuubangdev.portfolio.modules.user.domain.model.UserType;
import java.util.List;

public record UserResponse(
        String username,
        String email,
        List<Role> roles,
        Boolean enabled,
        UserType userType
) {}
