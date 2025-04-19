package ru.sshibko.STMS.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sshibko.STMS.dto.UserDto;
import ru.sshibko.STMS.mapper.UserMapper;
import ru.sshibko.STMS.model.User;
import ru.sshibko.STMS.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("getUserById() should return user DTO when user exists")
    void getUserByIdShouldReturnUserDtoWhenUserExists() {

        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .email("test@example.com")
                .firstName("Ivan")
                .lastName("Ivanov")
                .role("ROLE_USER")
                .build();

        UserDto expectedDto = UserDto.builder()
                .id(userId)
                .email("test@example.com")
                .firstName("Ivan")
                .lastName("Ivanov")
                .role("ROLE_USER")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(expectedDto);

        UserDto result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("Ivan", result.getFirstName());
        assertEquals("Ivanov", result.getLastName());
        assertEquals("ROLE_USER", result.getRole());

        verify(userRepository).findById(userId);
        verify(userMapper).toDto(user);
    }

    @Test
    @DisplayName("getUserById() should throw exception when user not found")
    void getUserByIdShouldThrowExceptionWhenUserNotFound() {

        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.getUserById(userId));

        assertEquals("User not found with id: " + userId, exception.getMessage());
        verify(userRepository).findById(userId);
        verify(userMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("getUserById() should map all fields correctly")
    void getUserByIdShouldMapAllFieldsCorrectly() {

        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .email("test@example.com")
                .firstName("Ivan")
                .lastName("Ivanov")
                .role("ROLE_ADMIN")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenCallRealMethod(); // Можно использовать реальный маппер

        UserDto result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getFirstName(), result.getFirstName());
        assertEquals(user.getLastName(), result.getLastName());
        assertEquals(user.getRole(), result.getRole());
    }
}
