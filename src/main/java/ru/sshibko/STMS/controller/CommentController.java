package ru.sshibko.STMS.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.sshibko.STMS.dto.CommentDto;
import ru.sshibko.STMS.dto.CommentRequest;
import ru.sshibko.STMS.service.CommentService;

@RestController
@RequestMapping("/api/v1/tasks/{taskId}/comments")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Comments", description = "Comment Management API")
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    @Operation(summary = "Get all comments for a task")
    public Page<CommentDto> getCommentByTaskId(@PathVariable Long taskId, Pageable pageable) {
        return commentService.getCommentsByTaskId(taskId, pageable);
    }

    @PostMapping
    @Operation(summary = "Add a comment to a task")
    public CommentDto addComment(@PathVariable Long taskId, @RequestBody CommentRequest commentRequest) {
        return commentService.addCommentToTask(taskId, commentRequest);
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "Delete a comment")
    public void deleteComment(@PathVariable Long commentId, @PathVariable Long taskId) {
        commentService.deleteComment(commentId);
    }
}
