package ru.sshibko.STMS.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.sshibko.STMS.dto.AuthResponse;
import ru.sshibko.STMS.dto.LoginRequest;
import ru.sshibko.STMS.dto.RegisterRequest;
import ru.sshibko.STMS.model.User;
import ru.sshibko.STMS.repository.UserRepository;
import ru.sshibko.STMS.security.JwtTokenProvider;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("register() Should Register new User")
    void registerShouldRegisterNewUser() {

        RegisterRequest request = RegisterRequest.builder()
                .email("test@example.com")
                .password("password")
                .firstName("Ivan")
                .lastName("Ivanov")
                .build();

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(new User());
        when(authenticationManager.authenticate(any())).thenReturn(mock(Authentication.class));
        when(tokenProvider.generateToken(any())).thenReturn("jwtToken");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("register() Should throw exception when Email already exists")
    void registerShouldThrowExceptionWhenEmailAlreadyExists() {

        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");

        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> authService.register(request));
    }

    @Test
    @DisplayName("login() Should return token")
    void loginShouldReturnToken() {

        LoginRequest request = LoginRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();

        when(authenticationManager.authenticate(any())).thenReturn(mock(Authentication.class));
        when(tokenProvider.generateToken(any())).thenReturn("jwtToken");

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
    }

}