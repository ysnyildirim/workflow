/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.repository;

import com.yil.workflow.model.ActionNext;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionTargetDao extends JpaRepository<ActionNext, Long> {

    boolean existsByActionIdAndTargetTypeId();
}
