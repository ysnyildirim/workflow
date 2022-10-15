/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */
package com.yil.workflow.repository;

import com.yil.workflow.model.ActionNotificationTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActionNotificationTargetDao extends JpaRepository<ActionNotificationTarget, Long> {

    List<ActionNotificationTarget> findAllByActionNotificationId(long actionNotificationId);
}
