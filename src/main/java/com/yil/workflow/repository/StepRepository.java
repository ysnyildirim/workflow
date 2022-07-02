package com.yil.workflow.repository;

import com.yil.workflow.model.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface StepRepository extends JpaRepository<Step, Long> {

    List<Step> findAllByFlowIdAndDeletedTimeIsNull(Long flowId);

    Optional<Step> findByIdAndDeletedTimeIsNull(Long id);

    Optional<Step> findByIdAndStepTypeIdAndDeletedTimeIsNull(Long id, Integer stepTypeId);

    List<Step> findByStepTypeIdAndEnabledTrueAndDeletedTimeIsNull(int stepTypeId);

    List<Step> findAllByFlowIdAndStepTypeIdAndEnabledTrueAndDeletedTimeIsNull(long flowId, int stepTypeId);

    boolean existsByIdAndStepTypeIdAndEnabledTrueAndDeletedTimeIsNull(long id, int stepTypeId);

    Optional<Step> findByIdAndFlowIdAndDeletedTimeIsNull(Long id, Long flowId);
    Optional<Step> findByIdAndEnabledTrueAndDeletedTimeIsNull(long id);
}
