package com.wuubangdev.portfolio.modules.social.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.modules.social.domain.model.Social;
import org.springframework.stereotype.Component;

@Component
public class SocialMapper {

    public Social toDomain(SocialJpaEntity entity) {
        return Social.builder()
                .id(entity.getId())
                .facebook(entity.getFacebook())
                .github(entity.getGithub())
                .linkedin(entity.getLinkedin())
                .zalo(entity.getZalo())
                .telegram(entity.getTelegram())
                .gmail(entity.getGmail())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .addressGgMapLink(entity.getAddressGgMapLink())
                .build();
    }

    public SocialJpaEntity toEntity(Social domain) {
        SocialJpaEntity entity = new SocialJpaEntity();
        entity.setId(domain.getId());
        entity.setFacebook(domain.getFacebook());
        entity.setGithub(domain.getGithub());
        entity.setLinkedin(domain.getLinkedin());
        entity.setZalo(domain.getZalo());
        entity.setTelegram(domain.getTelegram());
        entity.setGmail(domain.getGmail());
        entity.setPhone(domain.getPhone());
        entity.setAddress(domain.getAddress());
        entity.setAddressGgMapLink(domain.getAddressGgMapLink());
        return entity;
    }
}
