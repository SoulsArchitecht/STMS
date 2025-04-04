package ru.sshibko.STMS.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    private String email;

    private String password;

    private String firstName;

    private String lastName;
}
