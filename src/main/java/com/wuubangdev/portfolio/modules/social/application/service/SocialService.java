package com.wuubangdev.portfolio.modules.social.application.service;

import com.wuubangdev.portfolio.modules.social.application.dto.SocialRequest;
import com.wuubangdev.portfolio.modules.social.application.dto.SocialResponse;

public interface SocialService {
    SocialResponse getSocial();
    SocialResponse upsertSocial(SocialRequest request);
}
