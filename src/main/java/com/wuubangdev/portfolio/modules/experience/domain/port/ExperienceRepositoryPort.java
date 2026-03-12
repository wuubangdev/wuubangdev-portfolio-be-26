package com.wuubangdev.portfolio.modules.experience.domain.port;

import com.wuubangdev.portfolio.modules.experience.domain.model.Experience;

import java.util.List;
import java.util.Optional;

public interface ExperienceRepositoryPort {
    Experience save(Experience experience);
    List<Experience> findAll();
    Optional<Experience> findById(Long id);
    void deleteById(Long id);
}
