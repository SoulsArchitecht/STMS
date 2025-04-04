package ru.sshibko.STMS.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sshibko.STMS.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByTaskId(Long taskId, Pageable pageable);
}
