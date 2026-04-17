package com.wuubangdev.portfolio.modules.user.application.service;

import com.wuubangdev.portfolio.modules.user.application.dto.LoginRequest;
import com.wuubangdev.portfolio.modules.user.application.dto.LoginResponse;
import com.wuubangdev.portfolio.modules.user.application.dto.RegisterRequest;
import com.wuubangdev.portfolio.modules.user.application.dto.ForgotPasswordRequest;
import com.wuubangdev.portfolio.modules.user.application.dto.RefreshTokenRequest;
import com.wuubangdev.portfolio.modules.user.application.dto.ResetPasswordRequest;
import com.wuubangdev.portfolio.modules.user.application.dto.SocialLoginRequest;
import com.wuubangdev.portfolio.modules.user.application.dto.UserResponse;

public interface AuthService {
    void register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    LoginResponse refreshToken(RefreshTokenRequest request);
    LoginResponse loginWithGoogle(SocialLoginRequest request);
    LoginResponse loginWithGithub(SocialLoginRequest request);
    UserResponse getProfile(String username);
    void activateAccount(String token);
    void forgotPassword(ForgotPasswordRequest request);
    void resetPassword(ResetPasswordRequest request);
}
