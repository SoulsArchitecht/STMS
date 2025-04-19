package ru.sshibko.STMS.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.sshibko.STMS.dto.*;
import ru.sshibko.STMS.exception.ResourceNotFoundException;
import ru.sshibko.STMS.exception.UnauthorizedAccessException;
import ru.sshibko.STMS.mapper.TaskMapper;
import ru.sshibko.STMS.mapper.UserMapper;
import ru.sshibko.STMS.model.*;
import ru.sshibko.STMS.model.enums.TaskPriority;
import ru.sshibko.STMS.model.enums.TaskStatus;
import ru.sshibko.STMS.repository.TaskRepository;
import ru.sshibko.STMS.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private TaskService taskService;

    private final Pageable pageable = PageRequest.of(0, 10);

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("getAllTasks() Should return page of tasks")
    void getAllTasksShouldReturnPageOfTasks() {

        Task task = createTestTask();
        TaskDto taskDto = createTestTaskDto();
        Page<Task> taskPage = new PageImpl<>(Collections.singletonList(task));
        when(taskRepository.findAll(pageable)).thenReturn(taskPage);

        when(taskRepository.findAll(pageable)).thenReturn(taskPage);
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        Page<TaskDto> result = taskService.getAllTasks(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Task", result.getContent().get(0).getTitle());
        verify(taskRepository).findAll(pageable);
        verify(taskMapper).toDto(task);
    }

    @Test
    void getUserTasks_ShouldReturnPageOfTasks() {

        Task task = createTestTask();
        Page<Task> taskPage = new PageImpl<>(Collections.singletonList(task));
        when(taskRepository.findAllUserTasks(anyLong(), any(Pageable.class))).thenReturn(taskPage);

        Page<TaskDto> result = taskService.getUserTasks(1L, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(taskRepository).findAllUserTasks(1L, pageable);
    }

    @Test
    @DisplayName("getTaskById() Should return task when exists")
    void getTaskById_ShouldReturnTask_WhenExists() {

        Task task = createTestTask();
        TaskDto taskDto = createTestTaskDto();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        TaskDto result = taskService.getTaskById(1L);

        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        assertEquals(TaskStatus.PENDING, result.getStatus());
        verify(taskRepository).findById(1L);
        verify(taskMapper).toDto(task);
    }

    @Test
    @DisplayName("getTaskById() Should throw exception when not found")
    void getTaskByIdShouldThrowWhenNotFound() {

        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.getTaskById(1L));
        verify(taskRepository).findById(1L);
    }

    @Test
    @DisplayName("createTask() Should create and return task")
    void createTaskShouldCreateAndReturnTask() {

        TaskRequest request = new TaskRequest();
        request.setTitle("New Task");
        request.setDescription("Description");
        request.setAssigneeId(2L);

        User author = createTestUser(1L, "author@test.com", "ROLE_USER");
        User assignee = createTestUser(2L, "assignee@test.com", "ROLE_USER");
        Task task = createTestTask();
        TaskDto taskDto = createTestTaskDto();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("author@test.com");
        when(userRepository.findByEmail("author@test.com")).thenReturn(Optional.ofNullable(author));
        when(userRepository.findById(2L)).thenReturn(Optional.of(assignee));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        TaskDto result = taskService.createTask(request);

        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        verify(taskRepository).save(any(Task.class));
        verify(taskMapper).toDto(task);
    }

    @Test
    @DisplayName("updateTask() Should Update Task When Authorized")
    void updateTaskShouldUpdateTaskWhenAuthorized() {

        Task existingTask = createTestTask();
        User author = existingTask.getAuthor();

        TaskRequest request = new TaskRequest();
        request.setTitle("Updated Title");
        request.setDescription("Updated Description");
        request.setStatus(TaskStatus.IN_PROGRESS);
        request.setPriority(TaskPriority.HIGH);
        request.setAssigneeId(2L);

        Task updatedTask = Task.builder()
                .id(1L)
                .title("Updated Title")
                .description("Updated Description")
                .status(TaskStatus.IN_PROGRESS)
                .priority(TaskPriority.HIGH)
                .author(author)
                .assignee(existingTask.getAssignee())
                .createdAt(existingTask.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        TaskDto updatedTaskDto = TaskDto.builder()
                .id(1L)
                .title("Updated Title")
                .description("Updated Description")
                .status(TaskStatus.IN_PROGRESS)
                .priority(TaskPriority.HIGH)
                .authorId(author.getId())
                .assigneeId(existingTask.getAssignee().getId())
                .createdAt(existingTask.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(authentication.getName()).thenReturn("author@test.com");
        when(userRepository.findByEmail("author@test.com")).thenReturn(Optional.of(author));
        when(userRepository.findById(2L)).thenReturn(Optional.of(existingTask.getAssignee()));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);
        when(taskMapper.toDto(updatedTask)).thenReturn(updatedTaskDto);

        TaskDto result = taskService.updateTask(1L, request);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, result.getStatus());
        assertEquals(TaskPriority.HIGH, result.getPriority());

        verify(taskRepository).findById(1L);
        verify(userRepository).findByEmail("author@test.com");
        verify(taskRepository).save(any(Task.class));
        verify(taskMapper).toDto(updatedTask);
    }

    @Test
    @DisplayName("deleteTask() Should delete when admin")
    void deleteTaskShouldDeleteWhenAdmin() {

        Task task = createTestTask();
        User admin = createTestUser(3L, "admin@test.com", "ROLE_ADMIN");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(authentication.getName()).thenReturn("admin@test.com");
        when(userRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(admin));

        taskService.deleteTask(1L);

        verify(taskRepository).delete(task);
    }

    @Test
    @DisplayName("deleteTask() Should throw exception when not authorized")
    void deleteTaskShouldThrowWhenNotAuthorized() {

        Task task = createTestTask();
        User otherUser = createTestUser(3L, "other@test.com", "ROLE_USER");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(authentication.getName()).thenReturn("other@test.com");
        when(userRepository.findByEmail("other@test.com")).thenReturn(Optional.of(otherUser));

        assertThrows(UnauthorizedAccessException.class, () -> taskService.deleteTask(1L));
        verify(taskRepository, never()).delete(any());
    }

    private Task createTestTask() {
        User author = createTestUser(1L, "author@test.com", "ROLE_USER");
        User assignee = createTestUser(2L, "assignee@test.com", "ROLE_USER");

        return Task.builder()
                .id(1L)
                .title("Test Task")
                .description("Test Description")
                .status(TaskStatus.PENDING)
                .priority(TaskPriority.MEDIUM)
                .author(author)
                .assignee(assignee)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private User createTestUser(Long id, String email, String role) {
        return User.builder()
                .id(id)
                .email(email)
                .password("password")
                .firstName("First")
                .lastName("Last")
                .role(role)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private TaskDto createTestTaskDto() {
        return TaskDto.builder()
                .id(1L)
                .title("Test Task")
                .description("Test Description")
                .status(TaskStatus.PENDING)
                .priority(TaskPriority.MEDIUM)
                .authorId(1L)
                .assigneeId(2L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}