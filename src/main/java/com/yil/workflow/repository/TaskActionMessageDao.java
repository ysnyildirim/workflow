package com.yil.workflow.repository;

import com.yil.workflow.model.TaskActionMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskActionMessageDao extends JpaRepository<TaskActionMessage, Long> {
    Page<TaskActionMessage> findAllByTaskActionId(Pageable pageable, Long taskActionId);

    Optional<TaskActionMessage> findByIdAndTaskActionId(Long id, Long taskActionId);
}
