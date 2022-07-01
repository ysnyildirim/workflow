/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.repository;

import com.yil.workflow.model.ActionSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActionSourceDao extends JpaRepository<ActionSource, Long> {


    List<ActionSource> findAllByActionIdAndTargetTypeId(long actionId, int targetTypeId);

    List<ActionSource> findAllByActionId(long actionId);

    Optional<ActionSource> findByActionIdAndGroupIdAndTargetTypeId(Long actionId, Long groupId, Integer targetTypeId);

}
