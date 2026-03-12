package com.wuubangdev.portfolio.modules.profile.infrastructure.adapter.output.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileJpaRepository extends JpaRepository<ProfileJpaEntity, Long> {
    Optional<ProfileJpaEntity> findFirstByOrderByIdAsc();
}
