package ru.sshibko.STSM.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.sshibko.STSM.dto.UserDto;
import ru.sshibko.STSM.model.User;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toDto(User user);
}
