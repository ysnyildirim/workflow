/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */
package com.yil.workflow.repository;

import com.yil.workflow.model.StepType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StepTypeDao extends JpaRepository<StepType, Integer> {
}
