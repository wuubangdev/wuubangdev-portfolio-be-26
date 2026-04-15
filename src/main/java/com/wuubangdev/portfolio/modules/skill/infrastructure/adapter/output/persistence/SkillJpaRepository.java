package com.wuubangdev.portfolio.modules.skill.infrastructure.adapter.output.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SkillJpaRepository extends MongoRepository<SkillJpaEntity, Long> {}
