package com.ead.course.specifications;

import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.UUID;

public class SpecificationTemplate {

    public static Specification<ModuleModel> moduleByCourseId(final UUID id) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<CourseModel> course = query.from(CourseModel.class);
            Expression<Collection<ModuleModel>> modulesInCoursesCollection = course.get("modules");
            return cb.and(cb.equal(course.get("id"), id), cb.isMember(root, modulesInCoursesCollection));
        };
    }

    public static Specification<LessonModel> lessonByModuleId(final UUID id) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<ModuleModel> course = query.from(ModuleModel.class);
            Expression<Collection<LessonModel>> lessonsInModuleCollection = course.get("lessons");
            return cb.and(cb.equal(course.get("id"), id), cb.isMember(root, lessonsInModuleCollection));
        };
    }

    @And({
            @Spec(path = "courseLevel", spec = Equal.class),
            @Spec(path = "courseStatus", spec = Equal.class),
            @Spec(path = "title", spec = Like.class)
    })
    public interface CourseSpec extends Specification<CourseModel> {
    }

    @Spec(path = "title", spec = Like.class)
    public interface ModuleSpec extends Specification<ModuleModel> {
    }

    @Spec(path = "title", spec = Like.class)
    public interface LessonSpec extends Specification<LessonModel> {
    }

    public static Specification<CourseModel> coursesByUserId(UUID userId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Join<CourseModel, CourseUserModel> coursesByUserIdJoin = root.join("coursesByUserId");
            return cb.equal(coursesByUserIdJoin.get("userId"), userId);
        };
    }
}
