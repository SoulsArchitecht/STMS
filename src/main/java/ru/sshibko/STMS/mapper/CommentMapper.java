package ru.sshibko.STMS.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.sshibko.STMS.dto.CommentDto;
import ru.sshibko.STMS.model.Comment;

@Mapper
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(target = "author", source = "author")
    CommentDto toDto(Comment comment);
}
