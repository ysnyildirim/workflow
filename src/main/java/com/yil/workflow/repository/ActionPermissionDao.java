/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.repository;

import com.yil.workflow.model.ActionPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActionPermissionDao extends JpaRepository<ActionPermission, ActionPermission.Pk> {

    List<ActionPermission> findAllById_ActionId(Long actionId);
}
