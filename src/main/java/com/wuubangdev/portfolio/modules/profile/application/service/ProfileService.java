package com.wuubangdev.portfolio.modules.profile.application.service;

import com.wuubangdev.portfolio.modules.profile.application.dto.ProfileRequest;
import com.wuubangdev.portfolio.modules.profile.application.dto.ProfileResponse;
import com.wuubangdev.portfolio.modules.profile.application.dto.ProfileTranslationRequest;

public interface ProfileService {
    ProfileResponse getProfile();
    ProfileResponse upsertProfile(ProfileRequest request);
    ProfileResponse upsertTranslation(String locale, ProfileTranslationRequest request);
}
