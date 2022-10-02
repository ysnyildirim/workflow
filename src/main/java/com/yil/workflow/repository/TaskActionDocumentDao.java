package com.yil.workflow.repository;

import com.yil.workflow.model.TaskActionDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskActionDocumentDao extends JpaRepository<TaskActionDocument, Long> {
    Page<TaskActionDocument> findAllByTaskActionId(Pageable pageable, Long taskActionId);

    Optional<TaskActionDocument> findByIdAndTaskActionId(Long id, Long taskActionId);
}
