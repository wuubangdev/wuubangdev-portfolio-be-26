package com.wuubangdev.portfolio.modules.user.application.service;

import com.wuubangdev.portfolio.modules.user.application.dto.UserResponse;
import com.wuubangdev.portfolio.modules.user.domain.model.Role;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();
    UserResponse getUserById(Long id);
    UserResponse updateUserRoles(Long id, List<Role> roles);
}
