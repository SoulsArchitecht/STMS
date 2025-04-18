package ru.sshibko.STMS.mapper;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import ru.sshibko.STMS.dto.UserDto;
import ru.sshibko.STMS.model.User;

@Component
@RequiredArgsConstructor
public class UserMapper {

    public User toEntity(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .role(userDto.getRole())
                .build();
    }

    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .build();
    }
}
