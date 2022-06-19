package com.yil.workflow.repository;

import com.yil.workflow.model.Step;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface StepRepository extends JpaRepository<Step, Long> {

    Page<Step> findAllByFlowIdAndDeletedTimeIsNull(Pageable pageable, Long flowId);

    Optional<Step> findByIdAndDeletedTimeIsNull(Long id);

    Optional<Step> findByIdAndFlowIdAndDeletedTimeIsNull(Long id ,Long flowId);
}
