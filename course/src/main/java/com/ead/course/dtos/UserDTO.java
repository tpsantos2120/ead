package com.ead.course.dtos;

import com.ead.course.enums.UserStatus;
import com.ead.course.enums.UserType;
import lombok.Data;

import java.util.UUID;

@Data
public class UserDTO {

    private UUID id;
    private String username;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String cpf;
    private String imageUrl;
    private UserType userType;
    private UserStatus userStatus;
}
