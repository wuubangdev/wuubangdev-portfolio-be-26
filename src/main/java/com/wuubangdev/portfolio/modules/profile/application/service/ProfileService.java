package com.wuubangdev.portfolio.modules.profile.application.service;

import com.wuubangdev.portfolio.modules.profile.application.dto.ProfileRequest;
import com.wuubangdev.portfolio.modules.profile.application.dto.ProfileResponse;

public interface ProfileService {
    ProfileResponse getProfile();
    ProfileResponse upsertProfile(ProfileRequest request);
}
