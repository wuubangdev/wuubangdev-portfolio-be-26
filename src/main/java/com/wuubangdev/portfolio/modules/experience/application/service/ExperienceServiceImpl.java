package com.wuubangdev.portfolio.modules.experience.application.service;

import com.wuubangdev.portfolio.infrastructure.global.exception.ResourceNotFoundException;
import com.wuubangdev.portfolio.modules.experience.application.dto.ExperienceRequest;
import com.wuubangdev.portfolio.modules.experience.application.dto.ExperienceResponse;
import com.wuubangdev.portfolio.modules.experience.application.mapper.ExperienceApplicationMapper;
import com.wuubangdev.portfolio.modules.experience.domain.model.Experience;
import com.wuubangdev.portfolio.modules.experience.domain.port.ExperienceRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExperienceServiceImpl implements ExperienceService {

    private final ExperienceRepositoryPort experienceRepositoryPort;
    private final ExperienceApplicationMapper experienceApplicationMapper;

    @Override @Transactional
    public ExperienceResponse create(ExperienceRequest request) {
        Experience exp = experienceApplicationMapper.toNewDomain(request);
        return experienceApplicationMapper.toResponse(experienceRepositoryPort.save(exp));
    }

    @Override
    public List<ExperienceResponse> getAll() {
        return experienceRepositoryPort.findAll().stream().map(experienceApplicationMapper::toResponse).toList();
    }

    @Override @Transactional
    public ExperienceResponse update(Long id, ExperienceRequest request) {
        Experience exp = experienceRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience", id));
        Experience updatedExperience = experienceApplicationMapper.updateDomain(exp, request);
        return experienceApplicationMapper.toResponse(experienceRepositoryPort.save(updatedExperience));
    }

    @Override @Transactional
    public void delete(Long id) {
        experienceRepositoryPort.findById(id).orElseThrow(() -> new ResourceNotFoundException("Experience", id));
        experienceRepositoryPort.deleteById(id);
    }
}
