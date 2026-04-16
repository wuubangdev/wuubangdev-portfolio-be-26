package com.wuubangdev.portfolio.modules.setting.domain.port;

import com.wuubangdev.portfolio.modules.setting.domain.model.Setting;

import java.util.Optional;

public interface SettingRepositoryPort {
    Setting save(Setting setting);
    Optional<Setting> findFirst();
}
