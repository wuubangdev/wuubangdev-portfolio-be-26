package com.wuubangdev.portfolio.modules.experience.application.service;

import com.wuubangdev.portfolio.infrastructure.global.exception.ResourceNotFoundException;
import com.wuubangdev.portfolio.modules.experience.application.dto.ExperienceRequest;
import com.wuubangdev.portfolio.modules.experience.application.dto.ExperienceResponse;
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

    @Override @Transactional
    public ExperienceResponse create(ExperienceRequest request) {
        Experience exp = Experience.builder()
                .company(request.company()).role(request.role()).description(request.description())
                .logoUrl(request.logoUrl()).startDate(request.startDate()).endDate(request.endDate())
                .location(request.location()).displayOrder(request.displayOrder()).build();
        return toResponse(experienceRepositoryPort.save(exp));
    }

    @Override
    public List<ExperienceResponse> getAll() {
        return experienceRepositoryPort.findAll().stream().map(this::toResponse).toList();
    }

    @Override @Transactional
    public ExperienceResponse update(Long id, ExperienceRequest request) {
        Experience exp = experienceRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience", id));
        exp.setCompany(request.company()); exp.setRole(request.role()); exp.setDescription(request.description());
        exp.setLogoUrl(request.logoUrl()); exp.setStartDate(request.startDate()); exp.setEndDate(request.endDate());
        exp.setLocation(request.location()); exp.setDisplayOrder(request.displayOrder());
        return toResponse(experienceRepositoryPort.save(exp));
    }

    @Override @Transactional
    public void delete(Long id) {
        experienceRepositoryPort.findById(id).orElseThrow(() -> new ResourceNotFoundException("Experience", id));
        experienceRepositoryPort.deleteById(id);
    }

    private ExperienceResponse toResponse(Experience e) {
        return new ExperienceResponse(e.getId(), e.getCompany(), e.getRole(), e.getDescription(),
                e.getLogoUrl(), e.getStartDate(), e.getEndDate(), e.getLocation(), e.getDisplayOrder());
    }
}
