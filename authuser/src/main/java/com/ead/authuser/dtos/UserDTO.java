package com.ead.authuser.dtos;

import com.ead.authuser.validation.cpf.CpfConstraint;
import com.ead.authuser.validation.email.EmailConstraint;
import com.ead.authuser.validation.username.UsernameConstraint;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO implements UserView {

    private UUID id;

    @Size(groups = RegisterUser.class, min = 4, max = 50)
    @UsernameConstraint(groups = RegisterUser.class)
    @NotBlank(groups = RegisterUser.class)
    @JsonView(RegisterUser.class)
    private String username;

    @EmailConstraint(groups = RegisterUser.class)
    @NotBlank(groups = RegisterUser.class)
    @JsonView(RegisterUser.class)
    private String email;

    @Size(groups = {RegisterUser.class, UpdatePassword.class}, min = 6, max = 20)
    @NotBlank(groups = {RegisterUser.class, UpdatePassword.class})
    @JsonView({RegisterUser.class, UpdatePassword.class})
    private String password;

    @Size(groups = UpdatePassword.class, min = 6, max = 20)
    @NotBlank(groups = UpdatePassword.class)
    @JsonView(UpdatePassword.class)
    private String oldPassword;

    @JsonView({RegisterUser.class, UpdateUser.class})
    private String fullName;

    @JsonView({RegisterUser.class, UpdateUser.class})
    private String phoneNumber;

    @CpfConstraint(groups = {RegisterUser.class, UpdateUser.class})
    @JsonView({RegisterUser.class, UpdateUser.class})
    private String cpf;

    @NotBlank(groups = UpdateImage.class)
    @JsonView(UpdateImage.class)
    private String imageUrl;
}
