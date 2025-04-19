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
import ru.sshibko.STMS.dto.CommentDto;
import ru.sshibko.STMS.dto.CommentRequest;
import ru.sshibko.STMS.exception.ResourceNotFoundException;
import ru.sshibko.STMS.exception.UnauthorizedAccessException;
import ru.sshibko.STMS.mapper.CommentMapper;
import ru.sshibko.STMS.model.Comment;
import ru.sshibko.STMS.model.Task;
import ru.sshibko.STMS.model.User;
import ru.sshibko.STMS.repository.CommentRepository;
import ru.sshibko.STMS.repository.TaskRepository;
import ru.sshibko.STMS.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class CommentService {

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final TaskRepository taskRepository;

    private final CommentMapper commentMapper;

    public Page<CommentDto> getCommentsByTaskId(Long taskId, Pageable pageable) {
        log.info("getting comments by taskId {}", taskId);
        return commentRepository.findByTaskId(taskId, pageable)
                .map(commentMapper::toDto);
    }

    @Transactional
    public CommentDto addCommentToTask(@Valid Long taskId, @Valid CommentRequest commentRequest) {
        log.info("Adding comment to task {}", taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User commentAuthor = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        boolean isAuthor = task.getAuthor().getId().equals(commentAuthor.getId());
        boolean isAssignee = task.getAssignee() != null && task.getAssignee().getId().equals(commentAuthor.getId());
        boolean isAdmin = commentAuthor.getRole().equals("ROLE_ADMIN");

        if (!isAuthor && !isAssignee && !isAdmin) {
            throw new UnauthorizedAccessException("You are not authorized to add comment to this task");
        }

        Comment comment = Comment.builder()
                .text(commentRequest.getText())
                .task(task)
                .author(commentAuthor)
                .build();

        Comment savedComment = commentRepository.save(comment);

        return commentMapper.toDto(savedComment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        log.info("Deleting comment with id: {}", commentId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        if (!currentUser.getRole().equals("ROLE_ADMIN") && !comment.getAuthor().getId().equals(currentUser.getId())) {
            throw new UnauthorizedAccessException("You are not authorized to delete this comment");
        }

        commentRepository.delete(comment);
    }
}
