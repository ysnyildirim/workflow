package com.yil.workflow.repository;

import com.yil.workflow.model.TaskAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TaskActionDao extends JpaRepository<TaskAction, Long> {

    Page<TaskAction> findAllByTaskId(Pageable pageable, Long taskId);

    Optional<TaskAction> findByIdAndTaskId(Long id, Long taskId);

    @Query(nativeQuery = true,
            value = " select * from WFS.TASK_ACTION ta " +
                    " where ta.TASK_ID=:taskId" +
                    " and ta.Id = (select max(ta2.Id) from WFS.TASK_ACTION ta2 where ta2.TASK_ID=ta.TASK_ID)")
    Optional<TaskAction> getLastAction(@Param(value = "taskId") long taskId);

    boolean existsByIdAndCreatedUserId(long id, long createdUserId);

    @Query(nativeQuery = true,
            value = " select * from WFS.TASK_ACTION ta " +
                    " where ta.TASK_ID=:taskId" +
                    " and ta.Id = (select min(ta2.Id) from WFS.TASK_ACTION ta2 where ta2.TASK_ID=ta.TASK_ID)")
    Optional<TaskAction> getFirstAction(@Param(value = "taskId") long taskId);
}
