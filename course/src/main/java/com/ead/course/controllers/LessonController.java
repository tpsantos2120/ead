package com.ead.course.controllers;

import com.ead.course.dtos.LessonDTO;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.LessonService;
import com.ead.course.services.ModuleService;
import com.ead.course.specifications.SpecificationTemplate;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class LessonController {

    private final LessonService lessonService;
    private final ModuleService moduleService;

    public LessonController(LessonService lessonService, ModuleService moduleService) {
        this.lessonService = lessonService;
        this.moduleService = moduleService;
    }

    @PostMapping("/v1/modules/{moduleId}/lessons")
    public ResponseEntity<?> saveLesson(@RequestBody @Valid LessonDTO lessonDTO,
                                        @PathVariable(value = "moduleId") UUID moduleId) {

        log.debug("POST LessonController::saveLesson received moduleId {} and {}", moduleId, lessonDTO.toString());
        Optional<ModuleModel> moduleModelOptional = moduleService.findById(moduleId);
        if (moduleModelOptional.isEmpty()) {
            log.warn("Module not found with id {}", moduleId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module Not Found.");
        }
        var lessonModel = new LessonModel();
        BeanUtils.copyProperties(lessonDTO, lessonModel);
        lessonModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        lessonModel.setLastUpdatedDate(LocalDateTime.now(ZoneId.of("UTC")));
        lessonModel.setModule(moduleModelOptional.get());
        var savedLessonModel = lessonService.save(lessonModel);
        log.debug("POST LessonController::saveLesson saved {}", savedLessonModel.getId());
        log.info("Lesson saved successfully with id {}", savedLessonModel.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLessonModel);
    }

    @DeleteMapping("/v1/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<?> deleteLesson(@PathVariable(value = "moduleId") UUID moduleId,
                                          @PathVariable(value = "lessonId") UUID lessonId) {

        log.debug("DELETE LessonController::deleteLesson received moduleId {} and lessonId {}", moduleId, lessonId);
        Optional<LessonModel> lessonModelOptional = lessonService.findLessonByModuleId(moduleId, lessonId);
        if (lessonModelOptional.isEmpty()) {
            log.warn("Lesson not found with moduleId {}", moduleId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this module.");
        }
        lessonService.delete(lessonModelOptional.get());
        log.debug("DELETE LessonController::deleteLesson deleted {}", lessonModelOptional.get().getId());
        log.info("Lesson deleted successfully with id {}", lessonModelOptional.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body("Lesson deleted successfully.");
    }

    @PutMapping("/v1/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<?> updateLessonByModule(@PathVariable(value = "moduleId") UUID moduleId,
                                                  @PathVariable(value = "lessonId") UUID lessonId,
                                                  @RequestBody @Valid LessonDTO lessonDTO) {

        log.debug("PUT LessonController::updateLessonByModule received moduleId {} and lessonId {} and dto {}", moduleId, lessonId, lessonDTO.toString());
        Optional<LessonModel> lessonModelOptional = lessonService.findLessonByModuleId(moduleId, lessonId);
        if (lessonModelOptional.isEmpty()) {
            log.warn("Lesson not found with moduleId {}", moduleId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this module.");
        }
        BeanUtils.copyProperties(lessonDTO, lessonModelOptional.get());
        lessonModelOptional.get().setLastUpdatedDate(LocalDateTime.now(ZoneId.of("UTC")));
        log.debug("PUT LessonController::updateLessonByModule updated {}", lessonModelOptional.get().getId());
        log.info("Lesson updated successfully with id {}", lessonModelOptional.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body(lessonService.save(lessonModelOptional.get()));
    }

    @GetMapping("/v1/modules/{moduleId}/lessons")
    public ResponseEntity<Page<LessonModel>> getAllLessonsByModule(@PathVariable(value = "moduleId") UUID moduleId,
                                                                   SpecificationTemplate.LessonSpec spec,
                                                                   @PageableDefault(
                                                                           page = 0,
                                                                           size = 10,
                                                                           sort = "id",
                                                                           direction = Sort.Direction.ASC) Pageable pageable) {
        log.debug("GET LessonController::getAllLessonsByModule received moduleId {}", moduleId);
        return ResponseEntity.status(HttpStatus.OK).body(lessonService.findAllLessonsByCourse(
                SpecificationTemplate.lessonByModuleId(moduleId).and(spec), pageable));
    }

    @GetMapping("/v1/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<?> getOneLessonByModule(@PathVariable(value = "moduleId") UUID moduleId,
                                                  @PathVariable(value = "lessonId") UUID lessonId) {

        log.debug("GET LessonController::getOneLessonByModule received moduleId {} and lessonId {}", moduleId, lessonId);
        Optional<LessonModel> lessonModelOptional = lessonService.findLessonByModuleId(moduleId, lessonId);
        if (lessonModelOptional.isEmpty()) {
            log.warn("Lesson not found with moduleId {} and lessonId {}", moduleId, lessonId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this module.");
        }
        log.debug("GET LessonController::getOneLessonByModule received {}", lessonModelOptional.get().getId());
        log.info("Lesson retrieved successfully with id {}", lessonModelOptional.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body(lessonModelOptional.get());
    }

    @GetMapping("/v1/lessons")
    public ResponseEntity<Page<LessonModel>> getAllLessons(SpecificationTemplate.LessonSpec spec,
                                                           @PageableDefault(
                                                                   page = 0,
                                                                   size = 10,
                                                                   sort = "id",
                                                                   direction = Sort.Direction.ASC) Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(lessonService.findAll(spec, pageable));
    }

    @GetMapping("/v1/lessons/{lessonId}")
    public ResponseEntity<?> getLessonById(@PathVariable(value = "lessonId") UUID lessonId) {
        log.debug("GET LessonController::getLessonById received lessonId {}", lessonId);
        return ResponseEntity.status(HttpStatus.OK).body(lessonService.findById(lessonId));
    }
}
