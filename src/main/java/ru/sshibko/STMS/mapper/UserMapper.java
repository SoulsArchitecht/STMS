package ru.sshibko.STMS.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.sshibko.STMS.dto.UserDto;
import ru.sshibko.STMS.model.User;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toDto(User user);
}
