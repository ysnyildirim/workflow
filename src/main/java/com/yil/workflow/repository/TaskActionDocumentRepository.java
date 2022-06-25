package com.yil.workflow.repository;

import com.yil.workflow.model.TaskActionDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TaskActionDocumentRepository extends JpaRepository<TaskActionDocument, Long> {
    Page<TaskActionDocument> findAllByDeletedTimeIsNull(Pageable pageable);

    Page<TaskActionDocument> findAllByTaskActionIdAndDeletedTimeIsNull(Pageable pageable, Long taskActionId);

    Optional<TaskActionDocument> findByIdAndTaskActionIdAndDeletedTimeIsNull(Long id, Long taskActionId);

    Optional<TaskActionDocument> findByIdAndDeletedTimeIsNull(long id);
}
