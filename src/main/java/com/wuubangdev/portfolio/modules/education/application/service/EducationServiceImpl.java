package com.wuubangdev.portfolio.modules.education.application.service;

import com.wuubangdev.portfolio.modules.education.application.dto.EducationRequest;
import com.wuubangdev.portfolio.modules.education.application.dto.EducationResponse;
import com.wuubangdev.portfolio.modules.education.domain.model.Education;
import com.wuubangdev.portfolio.modules.education.domain.port.EducationRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EducationServiceImpl implements EducationService {

    private final EducationRepositoryPort educationRepositoryPort;

    @Override
    public List<EducationResponse> getAllEducations() {
        return educationRepositoryPort.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public EducationResponse getEducationById(Long id) {
        return educationRepositoryPort.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Education not found"));
    }

    @Override
    @Transactional
    public EducationResponse createEducation(EducationRequest request) {
        Education education = Education.builder()
                .institution(request.institution())
                .degree(request.degree())
                .fieldOfStudy(request.fieldOfStudy())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .description(request.description())
                .location(request.location())
                .displayOrder(request.displayOrder())
                .isPublic(request.isPublic())
                .build();
        return toResponse(educationRepositoryPort.save(education));
    }

    @Override
    @Transactional
    public EducationResponse updateEducation(Long id, EducationRequest request) {
        Education education = educationRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Education not found"));
        education.setInstitution(request.institution());
        education.setDegree(request.degree());
        education.setFieldOfStudy(request.fieldOfStudy());
        education.setStartDate(request.startDate());
        education.setEndDate(request.endDate());
        education.setDescription(request.description());
        education.setLocation(request.location());
        education.setDisplayOrder(request.displayOrder());
        education.setIsPublic(request.isPublic());
        return toResponse(educationRepositoryPort.save(education));
    }

    @Override
    @Transactional
    public void deleteEducation(Long id) {
        educationRepositoryPort.deleteById(id);
    }

    @Override
    @Transactional
    public EducationResponse setOrder(Long id, Integer order) {
        Education education = educationRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Education not found"));
        education.setDisplayOrder(order);
        return toResponse(educationRepositoryPort.save(education));
    }

    @Override
    @Transactional
    public EducationResponse togglePublic(Long id, Boolean isPublic) {
        Education education = educationRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Education not found"));
        education.setIsPublic(isPublic);
        return toResponse(educationRepositoryPort.save(education));
    }

    private EducationResponse toResponse(Education education) {
        return new EducationResponse(
                education.getId(),
                education.getInstitution(),
                education.getDegree(),
                education.getFieldOfStudy(),
                education.getStartDate(),
                education.getEndDate(),
                education.getDescription(),
                education.getLocation(),
                education.getDisplayOrder(),
                education.getIsPublic()
        );
    }
}
