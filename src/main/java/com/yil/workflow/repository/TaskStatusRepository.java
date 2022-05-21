package com.yil.workflow.repository;

import com.yil.workflow.model.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {
    Page<TaskStatus> findAllByDeletedTimeIsNull(Pageable pageable);
}
