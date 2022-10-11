package com.ead.course.models;

import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "TB_COURSES")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private UUID id;

    @Column(name = "TITLE", nullable = false, length = 150)
    private String title;

    @Column(name = "DESCRIPTION", nullable = false, length = 500)
    private String description;

    @Column(name = "COURSE_OUTLINE", length = 5000)
    private String courseOutline;

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "COURSE_STATUS", nullable = false)
    private CourseStatus courseStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "COURSE_LEVEL", nullable = false)
    private CourseLevel courseLevel;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "UPDATED_AT", nullable = false)
    private LocalDateTime lastUpdatedDate;

    @Column(name = "USER_INSTRUCTOR_ID", nullable = false)
    private UUID userInstructor;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private Set<ModuleModel> modules;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private Set<CourseUserModel> courses;
}
