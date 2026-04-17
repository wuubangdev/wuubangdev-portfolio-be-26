package com.wuubangdev.portfolio.modules.user.application.mapper;

import com.wuubangdev.portfolio.modules.user.application.dto.UserResponse;
import com.wuubangdev.portfolio.modules.user.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserApplicationMapper {

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getUsername(),
                user.getEmail(),
                user.getRoles(),
                !Boolean.FALSE.equals(user.getEnabled()),
                user.getUserType()
        );
    }
}
