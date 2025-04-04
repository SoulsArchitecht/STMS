package ru.sshibko.STSM.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.sshibko.STSM.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query(value = "SELECT t FROM Task t WHERE t.author.id = :userId")
    Page<Task> findAllByAuthorId(@Param("userId") Long userId, Pageable pageable);

    @Query(value = "SELECT t FROM Task t WHERE t.assignee.id = :userId")
    Page<Task> findAllByAssigneeId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.author.id = :userId OR t.assignee.id = : userId")
    Page<Task> findAllUserTasks(@Param("userId") Long userId, Pageable pageable);

}
