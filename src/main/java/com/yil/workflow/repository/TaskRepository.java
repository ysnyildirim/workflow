package com.yil.workflow.repository;

import com.yil.workflow.model.Target;
import com.yil.workflow.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findAllByDeletedTimeIsNull(Pageable pageable);
}
