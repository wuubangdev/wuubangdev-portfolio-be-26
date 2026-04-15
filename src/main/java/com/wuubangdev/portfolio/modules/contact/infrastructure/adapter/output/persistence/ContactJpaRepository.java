package com.wuubangdev.portfolio.modules.contact.infrastructure.adapter.output.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContactJpaRepository extends MongoRepository<ContactJpaEntity, Long> {}
