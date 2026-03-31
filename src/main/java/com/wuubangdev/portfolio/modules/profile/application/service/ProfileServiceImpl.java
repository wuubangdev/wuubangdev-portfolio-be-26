package com.wuubangdev.portfolio.modules.profile.application.service;

import com.wuubangdev.portfolio.modules.profile.application.dto.ProfileRequest;
import com.wuubangdev.portfolio.modules.profile.application.dto.ProfileResponse;
import com.wuubangdev.portfolio.modules.profile.application.mapper.ProfileApplicationMapper;
import com.wuubangdev.portfolio.modules.profile.domain.model.Profile;
import com.wuubangdev.portfolio.modules.profile.domain.port.ProfileRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepositoryPort profileRepositoryPort;
    private final ProfileApplicationMapper profileApplicationMapper;

    @Override
    public ProfileResponse getProfile() {
        return profileRepositoryPort.findFirst()
                .map(profileApplicationMapper::toResponse)
                .orElseGet(profileApplicationMapper::defaultResponse);
    }

    @Override
    @Transactional
    public ProfileResponse upsertProfile(ProfileRequest request) {
        Profile profile = profileRepositoryPort.findFirst()
                .orElseGet(Profile::new);

        Profile mappedProfile = profileApplicationMapper.toDomain(request, profile);
        return profileApplicationMapper.toResponse(profileRepositoryPort.save(mappedProfile));
    }
}
