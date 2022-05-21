package com.yil.workflow.repository;

import com.yil.workflow.model.Action;
import com.yil.workflow.model.Target;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TargetRepository extends JpaRepository<Target, Long> {
    Page<Target> findAllByDeletedTimeIsNull(Pageable pageable);
}
