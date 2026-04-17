package com.wuubangdev.portfolio.modules.education.application.service;

import com.wuubangdev.portfolio.modules.education.application.dto.EducationRequest;
import com.wuubangdev.portfolio.modules.education.application.dto.EducationResponse;
import com.wuubangdev.portfolio.modules.education.application.dto.EducationTranslationRequest;

import java.util.List;

public interface EducationService {
    List<EducationResponse> getAllEducations();
    EducationResponse getEducationById(Long id);
    EducationResponse createEducation(EducationRequest request);
    EducationResponse updateEducation(Long id, EducationRequest request);
    void deleteEducation(Long id);
    EducationResponse setOrder(Long id, Integer order);
    EducationResponse togglePublic(Long id, Boolean isPublic);
    EducationResponse upsertTranslation(Long id, String locale, EducationTranslationRequest request);
}
