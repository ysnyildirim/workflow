/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.repository;

import com.yil.workflow.model.FlowGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlowGroupDao extends JpaRepository<FlowGroup, Long> {

    Optional<FlowGroup> findByIdAndDeletedTimeIsNull(Long id);

    boolean existsByIdAndFlowId(Long id, Long flowId);

    List<FlowGroup> findAllByFlowIdAndDeletedTimeIsNull(Long flowId);
}
