package ru.sshibko.STMS.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
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
@Slf4j
@Validated
public class TaskService {

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    private final TaskMapper taskMapper;

    public Page<TaskDto> getAllTasks(Pageable pageable) {
        log.info("getting all tasks");
        return taskRepository.findAll(pageable)
                .map(taskMapper::toDto);
    }

    public Page<TaskDto> getUserTasks(Long userId, Pageable pageable) {
        log.info("getting user tasks");
        return taskRepository.findAllUserTasks(userId, pageable)
                .map(taskMapper::toDto);
    }

    public Page<TaskDto> getTasksByAuthorId(Long authorId, Pageable pageable) {
        log.info("getting tasks by authorId {}", authorId);
        return taskRepository.findAllByAuthorId(authorId, pageable)
                .map(taskMapper::toDto);
    }

    public Page<TaskDto> getTasksByAssigneeId(Long assigneeId, Pageable pageable) {
        log.info("getting tasks by assigneeId {}", assigneeId);
        return taskRepository.findAllByAssigneeId(assigneeId, pageable)
                .map(taskMapper::toDto);
    }

    public TaskDto getTaskById(Long id) {
        log.info("getting task by id {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        return taskMapper.toDto(task);
    }

    @Transactional
    public TaskDto createTask(@Valid TaskRequest taskRequest) {
        log.info("creating task with title {}", taskRequest.getTitle());
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

        return taskMapper.toDto(savedTask);
    }

    @Transactional
    public TaskDto updateTask(@Valid Long id, @Valid TaskRequest taskRequest) {
        log.info("updating task with id {}", id);
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

        return taskMapper.toDto(updatedTask);
    }

    @Transactional
    public void deleteTask(Long id) {
        log.info("deleting task with id {}", id);
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
