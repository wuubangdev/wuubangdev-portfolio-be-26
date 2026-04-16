package com.wuubangdev.portfolio.modules.setting.application.service;

import com.wuubangdev.portfolio.modules.setting.application.dto.SettingRequest;
import com.wuubangdev.portfolio.modules.setting.application.dto.SettingResponse;
import com.wuubangdev.portfolio.modules.setting.domain.model.Setting;
import com.wuubangdev.portfolio.modules.setting.domain.port.SettingRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SettingServiceImpl implements SettingService {

    private final SettingRepositoryPort settingRepositoryPort;

    @Override
    public SettingResponse getSetting() {
        return settingRepositoryPort.findFirst()
                .map(this::toResponse)
                .orElseGet(this::defaultResponse);
    }

    @Override
    @Transactional
    public SettingResponse upsertSetting(SettingRequest request) {
        Setting setting = settingRepositoryPort.findFirst()
                .orElseGet(Setting::new);

        setting.setLogo(request.logo());
        setting.setThumbnailImageSeo(request.thumbnailImageSeo());
        setting.setTitleSeo(request.titleSeo());
        setting.setDescriptionSeo(request.descriptionSeo());

        return toResponse(settingRepositoryPort.save(setting));
    }

    private SettingResponse toResponse(Setting setting) {
        return new SettingResponse(
                setting.getId(),
                setting.getLogo(),
                setting.getThumbnailImageSeo(),
                setting.getTitleSeo(),
                setting.getDescriptionSeo()
        );
    }

    private SettingResponse defaultResponse() {
        return new SettingResponse(null, "", "", "", "");
    }
}
