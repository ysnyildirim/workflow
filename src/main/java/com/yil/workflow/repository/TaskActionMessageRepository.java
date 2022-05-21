package com.yil.workflow.repository;

import com.yil.workflow.model.TaskActionDocument;
import com.yil.workflow.model.TaskActionMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TaskActionMessageRepository extends JpaRepository<TaskActionMessage, Long> {
    Page<TaskActionMessage> findAllByDeletedTimeIsNull(Pageable pageable);
}
