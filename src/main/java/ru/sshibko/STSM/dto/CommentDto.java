package ru.sshibko.STSM.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {

    private Long id;

    private String text;

    private UserDto author;

    LocalDateTime createdAt;
}
