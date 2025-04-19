package ru.sshibko.STMS.dto;

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

    private Long authorId;

    private Long taskId;

    private LocalDateTime createdAt;
}
