package com.ead.course.services;

import com.ead.course.models.LessonModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.UUID;

public interface LessonService {
    LessonModel save(LessonModel lessonModel);

    Optional<LessonModel> findLessonByModuleId(UUID moduleId, UUID lessonId);

    void delete(LessonModel lessonModel);

    Page<LessonModel> findAll(Specification<LessonModel> spec, Pageable pageable);

    Optional<LessonModel> findById(UUID lessonId);

    Page<LessonModel> findAllLessonsByCourse(Specification<LessonModel> spec, Pageable pageable);
}
