package com.yil.workflow.repository;

import com.yil.workflow.model.Step;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.concurrent.Flow;


@Repository
public interface StepRepository extends JpaRepository<Step, Long> {
    Page<Step> findAllByDeletedTimeIsNull(Pageable pageable);
}
