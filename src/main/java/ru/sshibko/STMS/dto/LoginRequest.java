package ru.sshibko.STMS.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {

    @NotNull
    @Email
    private String email;

    @NotBlank
    @Size(min = 8)
    private String password;
}
