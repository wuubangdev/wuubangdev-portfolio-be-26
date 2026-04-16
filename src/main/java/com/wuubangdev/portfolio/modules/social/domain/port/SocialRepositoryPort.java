package com.wuubangdev.portfolio.modules.social.domain.port;

import com.wuubangdev.portfolio.modules.social.domain.model.Social;

import java.util.Optional;

public interface SocialRepositoryPort {
    Social save(Social social);
    Optional<Social> findFirst();
}
