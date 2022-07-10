/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.repository;

import com.yil.workflow.model.ActionPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActionPermissionDao extends JpaRepository<ActionPermission, ActionPermission.Pk> {

    List<ActionPermission> findAllByPk_ActionId(Long id);
}
