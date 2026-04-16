package com.wuubangdev.portfolio.modules.setting.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.infrastructure.global.database.MongoSequenceService;
import com.wuubangdev.portfolio.modules.setting.domain.model.Setting;
import com.wuubangdev.portfolio.modules.setting.domain.port.SettingRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SettingPersistenceAdapter implements SettingRepositoryPort {

    private static final String SEQUENCE_NAME = "settings_sequence";

    private final SettingJpaRepository settingJpaRepository;
    private final SettingMapper settingMapper;
    private final MongoSequenceService sequenceService;

    @Override
    public Setting save(Setting setting) {
        SettingJpaEntity entity = settingMapper.toEntity(setting);
        if (entity.getId() == null) {
            entity.setId(sequenceService.nextId(SEQUENCE_NAME));
        }
        return settingMapper.toDomain(settingJpaRepository.save(entity));
    }

    @Override
    public Optional<Setting> findFirst() {
        return settingJpaRepository.findFirstByOrderByIdAsc()
                .map(settingMapper::toDomain);
    }
}
