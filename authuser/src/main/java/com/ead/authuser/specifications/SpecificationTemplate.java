package com.ead.authuser.specifications;

import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;

import java.util.UUID;

public class SpecificationTemplate {

    public static Specification<UserModel> usersByCourseId(UUID courseId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Join<UserModel, UserCourseModel> usersByCourseIdJoin = root.join("usersByCourseId");
            return cb.equal(usersByCourseIdJoin.get("courseId"), courseId);
        };
    }

    @And({
            @Spec(path = "userType", spec = Equal.class),
            @Spec(path = "userStatus", spec = Equal.class),
            @Spec(path = "email", spec = Like.class),
            @Spec(path = "username", spec = Like.class),
            @Spec(path = "cpf", spec = Equal.class),
            @Spec(path = "fullName", spec = Like.class),
    })
    public interface UserSpec extends Specification<UserModel> {

    }
}
