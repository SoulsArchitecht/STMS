package ru.sshibko.STMS.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.sshibko.STMS.exception.annotation.ValidTaskPriority;
import ru.sshibko.STMS.exception.annotation.ValidTaskStatus;
import ru.sshibko.STMS.model.enums.TaskPriority;
import ru.sshibko.STMS.model.enums.TaskStatus;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRequest {

    @NotBlank(message = "Title cannot be null")
    @Size(min = 1, max = 255, message = "Title length must be between 1 and 255")
    private String title;

    @Size(max = 10000, message = "Max length of the text is 10000")
    private String description;

    @ValidTaskStatus(message = "Status must have a value of PENDING, IN_PROGRESS, COMPLETED")
    private TaskStatus status;

    @ValidTaskPriority(message = "Priority must have a value of HIGH, MEDIUM, LOW")
    private TaskPriority priority;

    @Positive
    private Long assigneeId;
}
