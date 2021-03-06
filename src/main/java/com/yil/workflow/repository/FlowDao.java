package com.yil.workflow.repository;

import com.yil.workflow.model.Flow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlowDao extends JpaRepository<Flow, Long> {

    List<Flow> findAllByDeletedTimeIsNull();

    List<Flow> findAllByDeletedTimeIsNullAndEnabledTrue();

    Optional<Flow> findByIdAndDeletedTimeIsNull(Long id);

    boolean existsByIdAndEnabledTrueAndDeletedTimeIsNull(Long id);

}
