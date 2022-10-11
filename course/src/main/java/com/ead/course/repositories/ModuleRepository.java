package com.ead.course.repositories;

import com.ead.course.models.ModuleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModuleRepository extends JpaRepository<ModuleModel, UUID>, JpaSpecificationExecutor<ModuleModel> {

    @Query(value = "SELECT * FROM tb_modules WHERE course_id = :courseId", nativeQuery = true)
    List<ModuleModel> findAllModulesByCourseId(@Param("courseId") UUID courseId);

    @Query(value = "SELECT * FROM tb_modules WHERE course_id = :courseId AND id = :moduleId", nativeQuery = true)
    Optional<ModuleModel> findModuleByCourseId(@Param("courseId") UUID courseId, @Param("moduleId") UUID moduleId);
}
