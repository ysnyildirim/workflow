package com.yil.workflow.repository;

import com.yil.workflow.model.Action;
import com.yil.workflow.model.Step;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {
    Page<Action> findAllByStepIdAndDeletedTimeIsNull(Pageable pageable, Long stepId);
}
