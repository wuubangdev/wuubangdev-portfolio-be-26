package com.wuubangdev.portfolio.modules.setting.infrastructure.adapter.output.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SettingJpaRepository extends MongoRepository<SettingJpaEntity, Long> {
    Optional<SettingJpaEntity> findFirstByOrderByIdAsc();
}
