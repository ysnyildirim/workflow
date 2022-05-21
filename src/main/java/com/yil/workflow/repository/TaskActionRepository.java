package com.yil.workflow.repository;

import com.yil.workflow.model.Task;
import com.yil.workflow.model.TaskAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TaskActionRepository extends JpaRepository<TaskAction, Long> {
    Page<TaskAction> findAllByDeletedTimeIsNull(Pageable pageable);

    Page<TaskAction> findAllByAndTaskIdAndDeletedTimeIsNull(Pageable pageable, Long taskId);
}
