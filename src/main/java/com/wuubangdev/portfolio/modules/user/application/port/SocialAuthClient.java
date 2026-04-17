package com.wuubangdev.portfolio.modules.user.application.port;

import com.wuubangdev.portfolio.modules.user.application.dto.SocialUserProfile;

public interface SocialAuthClient {
    SocialUserProfile fetchUserProfile(String accessToken);
}
