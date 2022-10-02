package com.yil.workflow.repository;

import com.yil.workflow.model.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StepDao extends JpaRepository<Step, Long> {
    List<Step> findAllByFlowId(Long flowId);

    List<Step> findAllByFlowIdAndStepTypeIdAndEnabledTrue(long flowId, int stepTypeId);

    Optional<Step> findByIdAndFlowId(Long id, Long flowId);

    Optional<Step> findByIdAndEnabledTrue(long id);

    Boolean existsByIdAndStepTypeId(long id, int stepTypeId);
}
