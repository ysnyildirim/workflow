package com.yil.workflow.repository;

import com.yil.workflow.model.Flow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface FlowRepository extends JpaRepository<Flow, Long> {
    Page<Flow> findAllByDeletedTimeIsNull(Pageable pageable);

    Optional<Flow> findByIdAndDeletedTimeIsNull(Long id);
}
