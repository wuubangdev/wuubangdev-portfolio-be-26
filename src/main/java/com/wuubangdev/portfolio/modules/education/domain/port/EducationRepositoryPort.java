package com.wuubangdev.portfolio.modules.education.domain.port;

import com.wuubangdev.portfolio.modules.education.domain.model.Education;

import java.util.List;
import java.util.Optional;

public interface EducationRepositoryPort {
    Education save(Education education);
    List<Education> findAll();
    Optional<Education> findById(Long id);
    void deleteById(Long id);
}
