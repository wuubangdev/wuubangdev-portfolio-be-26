package com.wuubangdev.portfolio.modules.user.application.service;

import com.wuubangdev.portfolio.modules.user.application.dto.LoginRequest;
import com.wuubangdev.portfolio.modules.user.application.dto.LoginResponse;
import com.wuubangdev.portfolio.modules.user.application.dto.RegisterRequest;
import com.wuubangdev.portfolio.modules.user.application.dto.UserResponse;

public interface AuthService {
    void register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    UserResponse getProfile(String username);
}