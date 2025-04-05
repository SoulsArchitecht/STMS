package ru.sshibko.STMS.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.sshibko.STMS.dto.TaskDto;
import ru.sshibko.STMS.dto.TaskRequest;
import ru.sshibko.STMS.exception.ResourceNotFoundException;
import ru.sshibko.STMS.exception.UnauthorizedAccessException;
import ru.sshibko.STMS.mapper.TaskMapper;
import ru.sshibko.STMS.model.Task;
import ru.sshibko.STMS.model.User;
import ru.sshibko.STMS.model.enums.TaskPriority;
import ru.sshibko.STMS.model.enums.TaskStatus;
import ru.sshibko.STMS.repository.TaskRepository;
import ru.sshibko.STMS.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    public Page<TaskDto> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable)
                .map(TaskMapper.INSTANCE::toDto);
    }

    public Page<TaskDto> getUserTasks(Long userId, Pageable pageable) {
        return taskRepository.findAllUserTasks(userId, pageable)
                .map(TaskMapper.INSTANCE::toDto);
    }

    public Page<TaskDto> getTasksByAuthorId(Long authorId, Pageable pageable) {
        return taskRepository.findAllByAuthorId(authorId, pageable)
                .map(TaskMapper.INSTANCE::toDto);
    }

    public Page<TaskDto> getTasksByAssigneeId(Long assigneeId, Pageable pageable) {
        return taskRepository.findAllByAssigneeId(assigneeId, pageable)
                .map(TaskMapper.INSTANCE::toDto);
    }

    public TaskDto getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        return TaskMapper.INSTANCE.toDto(task);
    }

    @Transactional
    public TaskDto createTask(TaskRequest taskRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        User assignee = userRepository.findById(taskRequest.getAssigneeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + taskRequest.getAssigneeId()));

        Task task = Task.builder()
                .title(taskRequest.getTitle())
                .description(taskRequest.getDescription())
                .status(taskRequest.getStatus() != null ? taskRequest.getStatus() : TaskStatus.PENDING)
                .priority(taskRequest.getPriority() != null ? taskRequest.getPriority() : TaskPriority.MEDIUM)
                .author(author)
                .assignee(assignee)
                .build();

        Task savedTask = taskRepository.save(task);

        return TaskMapper.INSTANCE.toDto(savedTask);
    }

    @Transactional
    public TaskDto updateTask(Long id, TaskRequest taskRequest) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        if (!currentUser.getRole().equals("ROLE_ADMIN") && !task.getAuthor().getId().equals(currentUser.getId())) {
            throw new UnauthorizedAccessException("You do not have permission to update this task");
        }

        if (taskRequest.getTitle() != null) {
            task.setTitle(taskRequest.getTitle());
        }
        if (taskRequest.getDescription() != null) {
            task.setDescription(taskRequest.getDescription());
        }
        if (taskRequest.getStatus() != null) {
            task.setStatus(taskRequest.getStatus());
        }
        if (taskRequest.getPriority() != null) {
            task.setPriority(taskRequest.getPriority());
        }
        if (taskRequest.getAssigneeId() != null) {
            User assignee = userRepository.findById(taskRequest.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assignee not found with id: " + taskRequest.getAssigneeId()));
            task.setAssignee(assignee);
        }

        Task updatedTask = taskRepository.save(task);

        return TaskMapper.INSTANCE.toDto(updatedTask);
    }

    @Transactional
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        if (!currentUser.getRole().equals("ROLE_ADMIN") && !task.getAuthor().getId().equals(currentUser.getId())) {
            throw new UnauthorizedAccessException("You are not authorized to delete this task");
        }

        taskRepository.delete(task);
    }
}
