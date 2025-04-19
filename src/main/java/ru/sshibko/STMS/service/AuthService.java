package ru.sshibko.STMS.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.sshibko.STMS.dto.AuthResponse;
import ru.sshibko.STMS.dto.LoginRequest;
import ru.sshibko.STMS.dto.RegisterRequest;
import ru.sshibko.STMS.exception.EmailAlreadyExistsException;
import ru.sshibko.STMS.model.User;
import ru.sshibko.STMS.repository.UserRepository;
import ru.sshibko.STMS.security.JwtTokenProvider;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponse register(@Valid RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already in use");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole("ROLE_USER");

        User savedUser = userRepository.save(user);
        log.info("User registered with ID: {}", savedUser.getId());

        return authenticateUser(request.getEmail(), request.getPassword());
    }

    public AuthResponse login(@Valid LoginRequest request) {
        return authenticateUser(request.getEmail(), request.getPassword());
    }

    private AuthResponse authenticateUser(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        String jwt = tokenProvider.generateToken(authentication);
        return new AuthResponse(jwt);
    }
}
