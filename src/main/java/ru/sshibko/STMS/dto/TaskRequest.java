package ru.sshibko.STMS.dto;

import lombok.*;
import ru.sshibko.STMS.model.enums.TaskPriority;
import ru.sshibko.STMS.model.enums.TaskStatus;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRequest {

    private String title;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;

    private Long assigneeId;
}
