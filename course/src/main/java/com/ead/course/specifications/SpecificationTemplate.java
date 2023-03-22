package com.ead.course.specifications;

import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.models.UserModel;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

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

    public static Specification<UserModel> usersByCourseId(final UUID courseId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<CourseModel> course = query.from(CourseModel.class);
            Expression<Collection<UserModel>> coursesUsers = course.get("users");
            return cb.and(cb.equal(course.get("id"), courseId), cb.isMember(root, coursesUsers));
        };
    }

    public static Specification<CourseModel> coursesByUserId(final UUID userId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<UserModel> user = query.from(UserModel.class);
            Expression<Collection<CourseModel>> usersCourses = user.get("courses");
            return cb.and(cb.equal(user.get("id"), userId), cb.isMember(root, usersCourses));
        };
    }

    @And({
            @Spec(path = "courseLevel", spec = Equal.class),
            @Spec(path = "courseStatus", spec = Equal.class),
            @Spec(path = "title", spec = Like.class)
    })
    public interface CourseSpec extends Specification<CourseModel> {
    }

    @And({
            @Spec(path = "userType", spec = Equal.class),
            @Spec(path = "userStatus", spec = Equal.class),
            @Spec(path = "email", spec = Like.class),
            @Spec(path = "cpf", spec = Equal.class),
            @Spec(path = "fullName", spec = Like.class),
    })
    public interface UserSpec extends Specification<UserModel> {
    }

    @Spec(path = "title", spec = Like.class)
    public interface ModuleSpec extends Specification<ModuleModel> {
    }

    @Spec(path = "title", spec = Like.class)
    public interface LessonSpec extends Specification<LessonModel> {
    }
}
