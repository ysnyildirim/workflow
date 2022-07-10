/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.service;

import com.yil.workflow.model.ActionPermission;
import com.yil.workflow.repository.ActionPermissionDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ActionPermissionService {

    private final ActionPermissionDao actionPermissionDao;

    public List<ActionPermission> findAllByActionId(long actionId) {
        return actionPermissionDao.findAllByPk_ActionId(actionId);
    }

}
