package ru.sshibko.STMS.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sshibko.STMS.dto.TaskDto;
import ru.sshibko.STMS.exception.ResourceNotFoundException;
import ru.sshibko.STMS.model.Task;
import ru.sshibko.STMS.model.User;
import ru.sshibko.STMS.model.enums.TaskPriority;
import ru.sshibko.STMS.model.enums.TaskStatus;
import ru.sshibko.STMS.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class TaskMapper {

    private final UserRepository userRepository;

    public TaskDto toDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .priority(task.getPriority())
                .status(task.getStatus())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .authorId(task.getAuthor().getId())
                .assigneeId(task.getAssignee().getId())
                .build();
    }

    public Task toEntity(TaskDto taskDto) {
        User author = userRepository.findById(taskDto.getAuthorId()).orElseThrow(
                () -> new ResourceNotFoundException("User not found with ID " + taskDto.getAuthorId()));
        User assignee = userRepository.findById(taskDto.getAssigneeId()).orElseThrow(
                () -> new ResourceNotFoundException("User not found with ID " + taskDto.getAssigneeId()));

        TaskStatus status = null;
        if (taskDto.getStatus() != null) {
            try {
                status = taskDto.getStatus();
            } catch (ResourceNotFoundException e) {
                throw new IllegalArgumentException("Invalid task status " + taskDto.getStatus());
            }
        }

        TaskPriority priority = null;
        if (taskDto.getPriority() != null) {
            try {
                priority = taskDto.getPriority();
            } catch (ResourceNotFoundException e) {
                throw new IllegalArgumentException("Invalid task priority " + taskDto.getPriority());
            }
        }

        return Task.builder()
                .id(taskDto.getId())
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .status(status)
                .priority(priority)
                .author(author)
                .assignee(assignee)
                .build();
    }
}
