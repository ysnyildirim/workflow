package com.yil.workflow.repository;

import com.yil.workflow.model.TaskAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TaskActionRepository extends JpaRepository<TaskAction, Long> {
    Page<TaskAction> findAllByDeletedTimeIsNull(Pageable pageable);

    Page<TaskAction> findAllByAndTaskIdAndDeletedTimeIsNull(Pageable pageable, Long taskId);

    Optional<TaskAction> findByIdAndTaskIdAndDeletedTimeIsNull(Long id, Long taskId);
}
