package com.yil.workflow.repository;

import com.yil.workflow.model.TaskActionMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TaskActionMessageRepository extends JpaRepository<TaskActionMessage, Long> {
    Page<TaskActionMessage> findAllByDeletedTimeIsNull(Pageable pageable);

    Page<TaskActionMessage> findAllByTaskActionIdAndDeletedTimeIsNull(Pageable pageable, Long taskActionId);


    Optional<TaskActionMessage> findByIdAndTaskActionIdAndDeletedTimeIsNull(Long id, Long taskActionId);
}
