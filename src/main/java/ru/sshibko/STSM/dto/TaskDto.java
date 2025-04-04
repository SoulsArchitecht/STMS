package ru.sshibko.STSM.dto;

import lombok.*;
import ru.sshibko.STSM.model.enums.TaskPriority;
import ru.sshibko.STSM.model.enums.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDto {

    private Long id;

    private String title;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;

    private UserDto author;

    private UserDto assignee;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<CommentDto> comments;
}
