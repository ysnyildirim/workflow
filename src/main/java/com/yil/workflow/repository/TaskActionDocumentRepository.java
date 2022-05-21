package com.yil.workflow.repository;

import com.yil.workflow.model.TaskAction;
import com.yil.workflow.model.TaskActionDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TaskActionDocumentRepository extends JpaRepository<TaskActionDocument, Long> {
    Page<TaskActionDocument> findAllByDeletedTimeIsNull(Pageable pageable);

    Page<TaskActionDocument> findAllByTaskActionIdAndDeletedTimeIsNull(Pageable pageable, Long taskActionId);
}
