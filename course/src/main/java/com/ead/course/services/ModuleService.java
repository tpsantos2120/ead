package com.ead.course.services;

import com.ead.course.models.ModuleModel;
import com.ead.course.specifications.SpecificationTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.UUID;

public interface ModuleService {

    void delete(ModuleModel moduleModel);

    ModuleModel save(ModuleModel moduleModel);

    Optional<ModuleModel> findModuleByCourse(UUID courseId, UUID moduleId);

    Page<ModuleModel> findAll(SpecificationTemplate.ModuleSpec spec, Pageable pageable);

    Optional<ModuleModel> findById(UUID moduleId);

    Page<ModuleModel> findAllByCourse(Specification<ModuleModel> spec, Pageable pageable);
}
