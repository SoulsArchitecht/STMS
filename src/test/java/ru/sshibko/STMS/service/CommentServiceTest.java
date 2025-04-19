package ru.sshibko.STMS.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.sshibko.STMS.dto.CommentDto;
import ru.sshibko.STMS.dto.CommentRequest;

import ru.sshibko.STMS.exception.UnauthorizedAccessException;
import ru.sshibko.STMS.mapper.CommentMapper;
import ru.sshibko.STMS.model.Comment;
import ru.sshibko.STMS.model.Task;
import ru.sshibko.STMS.model.User;
import ru.sshibko.STMS.model.enums.TaskPriority;
import ru.sshibko.STMS.model.enums.TaskStatus;
import ru.sshibko.STMS.repository.CommentRepository;
import ru.sshibko.STMS.repository.TaskRepository;
import ru.sshibko.STMS.repository.UserRepository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CommentService commentService;

    private final Pageable pageable = PageRequest.of(0, 10);

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("getCommentsByTaskId() Should return page of comments") //проходит
    void getCommentsByTaskId_ShouldReturnPageOfComments() {

        Comment comment = createTestComment();
        Page<Comment> commentPage = new PageImpl<>(List.of(comment));
        when(commentRepository.findByTaskId(anyLong(), any(Pageable.class))).thenReturn(commentPage);

        Page<CommentDto> result = commentService.getCommentsByTaskId(1L, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(commentRepository).findByTaskId(1L, pageable);
    }

    @Test
    @DisplayName("addComment() Should add comment to task when authorized") //не проходит
    void addCommentToTaskShouldAddCommentWhenAuthorized() {

        Long taskId = 1L;
        CommentRequest request = new CommentRequest();
        request.setText("Test comment");

        User author = createTestUser(1L, "author@test.com", "ROLE_USER");
        Task task = createTestTask();
        task.setAuthor(author);

        Comment savedComment = createTestComment();
        savedComment.setId(1L);
        savedComment.setAuthor(author);
        savedComment.setTask(task);

        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("Test comment")
                .authorId(author.getId())
                .taskId(task.getId())
                .createdAt(LocalDateTime.now())
                .build();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("author@test.com");
        when(userRepository.findByEmail("author@test.com")).thenReturn(Optional.of(author));
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);
        when(commentMapper.toDto(savedComment)).thenReturn(commentDto);

        CommentDto result = commentService.addCommentToTask(taskId, request);

        assertNotNull(result);
        assertEquals("Test comment", result.getText());
        verify(commentRepository).save(any(Comment.class));
        verify(commentMapper).toDto(savedComment);
    }

    @Test
    @DisplayName("addComment() Should throw exception when not authorized") //проходит
    void addCommentToTaskShouldThrowWhenNotAuthorized() {

        Long taskId = 1L;
        CommentRequest request = new CommentRequest();
        request.setText("Test comment");

        User author = createTestUser(3L, "author3@test.com", "ROLE_USER");
        User otherUser = createTestUser(4L, "other4@test.com", "ROLE_USER");
        Task task = createTestTask();
        task.setAuthor(author);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("other@test.com");
        when(userRepository.findByEmail("other@test.com")).thenReturn(Optional.of(otherUser));
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        assertThrows(UnauthorizedAccessException.class,
                () -> commentService.addCommentToTask(taskId, request));
        verify(commentRepository, never()).save(any());

        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("deleteComment() Should delete when authorized") //проходит
    void deleteCommentShouldDeleteWhenAuthorized() {

        Comment comment = createTestComment();
        User author = comment.getAuthor();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(authentication.getName()).thenReturn("author@test.com");
        when(userRepository.findByEmail("author@test.com")).thenReturn(Optional.of(author));

        commentService.deleteComment(1L);

        verify(commentRepository).delete(comment);

        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("deleteComment() Should throw exception when not authorized") //проходит
    void deleteCommentShouldThrowWhenNotAuthorized() {

        Comment comment = createTestComment();
        User otherUser = createTestUser(2L, "other@test.com", "ROLE_USER");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(authentication.getName()).thenReturn("other@test.com");
        when(userRepository.findByEmail("other@test.com")).thenReturn(Optional.of(otherUser));

        assertThrows(UnauthorizedAccessException.class,
                () -> commentService.deleteComment(1L));
        verify(commentRepository, never()).delete(any());

        SecurityContextHolder.clearContext();
    }

    private Comment createTestComment() {
        LocalDateTime timestamp = LocalDateTime.now();
        User author = createTestUser(1L, "author@test.com", "ROLE_USER");
        Task task = createTestTask();

        return Comment.builder()
                .id(1L)
                .text("Test comment")
                .author(author)
                .task(task)
                .createdAt(timestamp)
                .build();
    }

    private Task createTestTask() {
        LocalDateTime timestamp = LocalDateTime.now();
        User author = createTestUser(1L, "author@test.com", "ROLE_USER");
        User assignee = createTestUser(2L, "assignee@test.com", "ROLE_USER");

        return Task.builder()
                .id(1L)
                .title("Test Task")
                .description("Test description")
                .status(TaskStatus.PENDING)
                .priority(TaskPriority.MEDIUM)
                .author(author)
                .assignee(assignee)
                .createdAt(timestamp)
                .updatedAt(timestamp)
                .build();
    }

    private User createTestUser(Long id, String email, String role) {
        return User.builder()
                .id(id)
                .email(email)
                .password("password")
                .role(role)
                .build();
    }



}