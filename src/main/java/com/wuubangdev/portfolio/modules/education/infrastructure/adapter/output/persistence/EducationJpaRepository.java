package com.wuubangdev.portfolio.modules.education.infrastructure.adapter.output.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface EducationJpaRepository extends MongoRepository<EducationJpaEntity, Long> {
}
