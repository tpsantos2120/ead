package com.ead.authuser.mappers;

import com.ead.authuser.dtos.UserDTO;
import com.ead.authuser.dtos.UserEventDto;
import com.ead.authuser.models.UserModel;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "actionType", ignore = true)
    UserEventDto userModelToUserEventDto(UserModel userModel);

    @Mapping(target = "userType", ignore = true)
    @Mapping(target = "userStatus", ignore = true)
    @Mapping(target = "lastUpdatedDate", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    UserModel userDtoToUserModel(UserDTO userDto);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "cpf", source = "cpf")
    void updateUserModel(UserDTO userDTO, @MappingTarget UserModel userModel);
}
