package com.yil.workflow.repository;

import com.yil.workflow.model.Action;
import com.yil.workflow.model.ActionTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ActionTargetRepository extends JpaRepository<ActionTarget, Long> {
    Page<ActionTarget> findAllByDeletedTimeIsNull(Pageable pageable);
}
