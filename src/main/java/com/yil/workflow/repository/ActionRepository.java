package com.yil.workflow.repository;

import com.yil.workflow.model.Action;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {
    Page<Action> findAllByStepIdAndDeletedTimeIsNull(Pageable pageable, Long stepId);

    Action findByIdAndStepIdAndDeletedTimeIsNull(Long id, Long stepId);

    Optional<Action> findByIdAndStepId(Long id, Long stepId);

    Optional<Action> findByIdAndEnabledTrueAndDeletedTimeIsNull(Long id);

    Optional<Action> findByIdAndDeletedTimeIsNull(Long id);

}
