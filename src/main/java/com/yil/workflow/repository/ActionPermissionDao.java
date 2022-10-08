/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */
package com.yil.workflow.repository;

import com.yil.workflow.model.ActionPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActionPermissionDao extends JpaRepository<ActionPermission, Integer> {
    List<ActionPermission> findAllByActionId(Long actionId);

    void deleteAllByActionIdAndId(long actionId, int id);

    boolean existsAllByActionIdAndActionPermissionTypeId(long actionId, int actionPermissionTypeId);

    List<ActionPermission> findAllByActionIdAndActionPermissionTypeId(long actionId, int actionPermissionTypeId);
}
