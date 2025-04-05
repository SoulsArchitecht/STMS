package ru.sshibko.STMS.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sshibko.STMS.dto.UserDto;
import ru.sshibko.STMS.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public UserDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}
