package ru.sshibko.STMS.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sshibko.STMS.dto.UserDto;
import ru.sshibko.STMS.mapper.UserMapper;
import ru.sshibko.STMS.model.User;
import ru.sshibko.STMS.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return UserMapper.INSTANCE.toDto(user);
    }
}
