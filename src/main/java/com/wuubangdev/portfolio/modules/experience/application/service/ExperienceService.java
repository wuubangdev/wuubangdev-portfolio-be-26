package com.wuubangdev.portfolio.modules.experience.application.service;

import com.wuubangdev.portfolio.modules.experience.application.dto.ExperienceRequest;
import com.wuubangdev.portfolio.modules.experience.application.dto.ExperienceResponse;

import java.util.List;

public interface ExperienceService {
    ExperienceResponse create(ExperienceRequest request);
    List<ExperienceResponse> getAll();
    ExperienceResponse update(Long id, ExperienceRequest request);
    void delete(Long id);
}
