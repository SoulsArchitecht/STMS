package ru.sshibko.STMS.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.sshibko.STMS.dto.LoginRequest;
import ru.sshibko.STMS.dto.RegisterRequest;
import ru.sshibko.STMS.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login user")
    public String login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    @Operation(summary = "Register new user")
    public String register(@RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }
}
