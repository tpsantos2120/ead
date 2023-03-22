package com.ead.authuser.mappers;

import com.ead.authuser.dtos.UserEventDto;
import com.ead.authuser.models.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {
    @Mapping(target = "actionType", ignore = true)
    UserEventDto entityToDTO(UserModel userModel);

    List<UserEventDto> entityToDTO(Iterable<UserModel> userModels);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "lastUpdatedDate", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    UserModel dtoToEntity(UserEventDto userEventDto);

    List<UserModel> dtoToEntity(Iterable<UserEventDto> userEventDtos);
}
