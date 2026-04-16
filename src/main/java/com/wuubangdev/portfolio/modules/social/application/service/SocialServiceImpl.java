package com.wuubangdev.portfolio.modules.social.application.service;

import com.wuubangdev.portfolio.modules.social.application.dto.SocialRequest;
import com.wuubangdev.portfolio.modules.social.application.dto.SocialResponse;
import com.wuubangdev.portfolio.modules.social.domain.model.Social;
import com.wuubangdev.portfolio.modules.social.domain.port.SocialRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SocialServiceImpl implements SocialService {

    private final SocialRepositoryPort socialRepositoryPort;

    @Override
    public SocialResponse getSocial() {
        return socialRepositoryPort.findFirst()
                .map(this::toResponse)
                .orElseGet(this::defaultResponse);
    }

    @Override
    @Transactional
    public SocialResponse upsertSocial(SocialRequest request) {
        Social social = socialRepositoryPort.findFirst()
                .orElseGet(Social::new);

        social.setFacebook(request.facebook());
        social.setGithub(request.github());
        social.setLinkedin(request.linkedin());
        social.setZalo(request.zalo());
        social.setTelegram(request.telegram());
        social.setGmail(request.gmail());
        social.setPhone(request.phone());
        social.setAddress(request.address());
        social.setAddressGgMapLink(request.addressGgMapLink());

        return toResponse(socialRepositoryPort.save(social));
    }

    private SocialResponse toResponse(Social social) {
        return new SocialResponse(
                social.getId(),
                social.getFacebook(),
                social.getGithub(),
                social.getLinkedin(),
                social.getZalo(),
                social.getTelegram(),
                social.getGmail(),
                social.getPhone(),
                social.getAddress(),
                social.getAddressGgMapLink()
        );
    }

    private SocialResponse defaultResponse() {
        return new SocialResponse(null, "", "", "", "", "", "", "", "", "");
    }
}
