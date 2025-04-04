package ru.sshibko.STSM.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.sshibko.STSM.dto.TaskDto;
import ru.sshibko.STSM.model.Task;

@Mapper(uses = {UserMapper.class, CommentMapper.class})
public interface TaskMapper {
    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    @Mapping(target = "author", source = "author")
    @Mapping(target = "assignee", source = "assignee")
    @Mapping(target = "comments", source = "comments")
    TaskDto toDto(Task task);
}
