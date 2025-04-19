package ru.sshibko.STMS.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sshibko.STMS.dto.UserDto;
import ru.sshibko.STMS.mapper.UserMapper;
import ru.sshibko.STMS.model.User;
import ru.sshibko.STMS.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserDto getUserById(Long id) {
        log.info("getting user by id {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return userMapper.toDto(user);
    }
}
