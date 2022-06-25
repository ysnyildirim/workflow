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
public interface TaskActionRepository extends JpaRepository<TaskAction, Long> {

    Page<TaskAction> findAllByTaskIdAndDeletedTimeIsNull(Pageable pageable, Long taskId);

    Optional<TaskAction> findByIdAndTaskIdAndDeletedTimeIsNull(Long id, Long taskId);

    Optional<TaskAction> findByIdAndDeletedTimeIsNull(Long id);

    @Query(nativeQuery = true,
            value = " select * from TASK_ACTION ta " +
                    " where ta.TASK_ID=:p_task_id" +
                    " and ta.Id = (select max(ta2.Id) from TASK_ACTION ta2 where ta2.TASK_ID=ta.TASK_ID)")
    TaskAction getLastAction(@Param(value = "p_task_id") long taskId);
}
