package com.ead.course.services.impl;

import com.ead.course.models.LessonModel;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.services.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;

    @Autowired
    public LessonServiceImpl(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    @Override
    public LessonModel save(LessonModel lessonModel) {
        return lessonRepository.save(lessonModel);
    }

    @Override
    public Optional<LessonModel> findLessonByModuleId(UUID moduleId, UUID lessonId) {
        return lessonRepository.findLessonByModuleId(moduleId, lessonId);
    }

    @Override
    public void delete(LessonModel lessonModel) {
        lessonRepository.delete(lessonModel);
    }

    @Override
    public Page<LessonModel> findAll(Specification<LessonModel> spec, Pageable pageable) {
        return lessonRepository.findAll(spec, pageable);
    }

    @Override
    public Optional<LessonModel> findById(UUID lessonId) {
        return lessonRepository.findById(lessonId);
    }

    @Override
    public Page<LessonModel> findAllLessonsByCourse(Specification<LessonModel> spec, Pageable pageable) {
        return lessonRepository.findAll(spec, pageable);
    }
}
