package ru.sshibko.STMS.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    @NotBlank(message = "Email cannot be null")
    @Email(message = "email must be in correct form")
    private String email;

    @NotBlank(message = "Password cannot be null")
    @Size(min = 8, message = "Password minimum length is 8")
    private String password;

    @NotBlank(message = "Firstname cannot be null")
    @Size(min = 1, max = 255, message = "Firstname length must be between 1 and 255")
    private String firstName;

    @NotBlank(message = "Lastname cannot be null")
    @Size(min = 1, max = 255, message = "Lastname length must be between 1 and 255")
    private String lastName;
}
