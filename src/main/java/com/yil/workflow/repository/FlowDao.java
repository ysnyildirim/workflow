package com.yil.workflow.repository;

import com.yil.workflow.model.Flow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlowDao extends JpaRepository<Flow, Long> {
    List<Flow> findAllByEnabledTrue();
}
