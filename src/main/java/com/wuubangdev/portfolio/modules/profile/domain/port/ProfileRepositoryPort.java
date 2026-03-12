package com.wuubangdev.portfolio.modules.profile.domain.port;

import com.wuubangdev.portfolio.modules.profile.domain.model.Profile;

import java.util.Optional;

public interface ProfileRepositoryPort {
    Profile save(Profile profile);
    Optional<Profile> findFirst();
}
