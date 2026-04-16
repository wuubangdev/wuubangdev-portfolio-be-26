package com.wuubangdev.portfolio.modules.setting.application.service;

import com.wuubangdev.portfolio.modules.setting.application.dto.SettingRequest;
import com.wuubangdev.portfolio.modules.setting.application.dto.SettingResponse;

public interface SettingService {
    SettingResponse getSetting();
    SettingResponse upsertSetting(SettingRequest request);
}
