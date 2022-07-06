/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.repository;

import com.yil.workflow.model.TaskActionTarget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskActionTargetDao  extends JpaRepository<TaskActionTarget,Long> {
}
