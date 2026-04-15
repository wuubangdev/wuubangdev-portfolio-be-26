package com.wuubangdev.portfolio.modules.experience.infrastructure.adapter.output.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExperienceJpaRepository extends MongoRepository<ExperienceJpaEntity, Long> {}
