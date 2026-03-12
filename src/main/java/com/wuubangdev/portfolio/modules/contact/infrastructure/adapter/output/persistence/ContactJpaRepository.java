package com.wuubangdev.portfolio.modules.contact.infrastructure.adapter.output.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactJpaRepository extends JpaRepository<ContactJpaEntity, Long> {}
