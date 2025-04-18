package ru.sshibko.STMS.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sshibko.STMS.dto.CommentDto;
import ru.sshibko.STMS.exception.ResourceNotFoundException;
import ru.sshibko.STMS.model.Comment;
import ru.sshibko.STMS.model.Task;
import ru.sshibko.STMS.model.User;
import ru.sshibko.STMS.repository.TaskRepository;
import ru.sshibko.STMS.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    public CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .createdAt(comment.getCreatedAt())
                .authorId(comment.getAuthor().getId())
                .taskId(comment.getTask().getId())
                .build();
    }

    public Comment toEntity(CommentDto commentDto) {
        Task task = taskRepository.findById(commentDto.getTaskId()).orElseThrow(
                () -> new ResourceNotFoundException("Task not found with ID " + commentDto.getTaskId()));

        User author = userRepository.findById(commentDto.getAuthorId()).orElseThrow(
                () -> new ResourceNotFoundException("User not found with ID " + commentDto.getAuthorId()));

        return Comment.builder()
                .id(commentDto.getId())
                .text(commentDto.getText())
                .author(author)
                .task(task)
                .build();
    }
}
