package ru.sshibko.STSM.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private String role;
}
