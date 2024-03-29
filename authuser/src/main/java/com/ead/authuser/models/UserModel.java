package com.ead.authuser.models;

import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(
        name = "TB_USERS",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_username_unique",
                        columnNames = "USERNAME"
                ),
                @UniqueConstraint(
                        name = "uk_email_unique",
                        columnNames = "EMAIL"
                ),
                @UniqueConstraint(
                        name = "uk_cpf_unique",
                        columnNames = "CPF"
                ),
        })
public class UserModel extends RepresentationModel<UserModel> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private UUID id;

    @Column(name = "USERNAME", nullable = false, length = 50)
    private String username;

    @Column(name = "EMAIL", nullable = false, length = 50)
    private String email;

    @JsonIgnore
    @Column(name = "PASSWORD", nullable = false, length = 256)
    private String password;

    @Column(name = "FULLNAME", nullable = false, length = 150)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(name = "USER_STATUS", nullable = false)
    private UserStatus userStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "USER_TYPE", nullable = false)
    private UserType userType;

    @Column(name = "PHONE_NUMBER", length = 20)
    private String phoneNumber;

    @Column(name = "CPF", length = 20, nullable = false)
    private String cpf;

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "UPDATED_AT", nullable = false)
    private LocalDateTime lastUpdatedDate;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<UserCourseModel> usersByCourseId;

    public UserCourseModel convertToUserCourseModel(UUID courseId) {
        return new UserCourseModel(null, this, courseId);
    }
}
