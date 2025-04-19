package ru.sshibko.STMS.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.sshibko.STMS.dto.TaskDto;
import ru.sshibko.STMS.dto.TaskRequest;
import ru.sshibko.STMS.service.TaskService;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Tasks", description = "Task Management API")
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    @Operation(summary = "Get all tasks (Admin only)")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<TaskDto> getAllTasks(Pageable pageable) {
        return taskService.getAllTasks(pageable);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get all tasks for a specific user")
    public Page<TaskDto> getAllUserTasks(@PathVariable Long userId, Pageable pageable) {
        return taskService.getUserTasks(userId, pageable);
    }

    @GetMapping("/author/{authorId}")
    @Operation(summary = "Get all tasks by author")
    public Page<TaskDto> getAllAuthorTasks(@PathVariable Long authorId, Pageable pageable) {
        return taskService.getTasksByAuthorId(authorId, pageable);
    }

    @GetMapping("/assignee/{assigneeId}")
    @Operation(summary = "Get all tasks assigned to a user")
    public Page<TaskDto> getAllAssigneeTasks(@PathVariable Long assigneeId, Pageable pageable) {
        return taskService.getTasksByAssigneeId(assigneeId, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID")
    public TaskDto getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PostMapping
    @Operation(summary = "Create a new task")
    public TaskDto createTask(@RequestBody TaskRequest taskRequest) {
        return taskService.createTask(taskRequest);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a task")
    public TaskDto updateTask(@PathVariable Long id, @RequestBody TaskRequest taskRequest) {
        return taskService.updateTask(id, taskRequest);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
