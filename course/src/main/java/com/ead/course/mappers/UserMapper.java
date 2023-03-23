package com.ead.course.mappers;


import com.ead.course.dtos.UserEventDto;
import com.ead.course.models.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "courses", ignore = true)
    UserModel userEventDtoToUserModel(UserEventDto userEventDto);

}
