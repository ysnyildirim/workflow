/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.service;

import com.yil.workflow.repository.ActionPermissionDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActionPermissionService {
    private final ActionPermissionDao actionPermissionDao;

    public boolean existsByActionIdAndTargetTypeIdAndUserId(long actionId, int targetTypeId, long userId) {
        return actionPermissionDao.existsByActionIdAndTargetTypeIdAndUserId(actionId, targetTypeId, userId);
    }

    public boolean existsByActionIdAndTargetTypeIdAndGroupId(long actionId, int targetTypeId, long groupId) {
        return actionPermissionDao.existsByActionIdAndTargetTypeIdAndGroupId(actionId, targetTypeId, groupId);
    }

    public boolean existsByActionIdAndTargetTypeId(Long actionId, int targetTypeId) {
        return actionPermissionDao.existsByActionIdAndTargetTypeId(actionId, targetTypeId);
    }

    public boolean availableActionInUserId(long actionId, long userId) {
        return actionPermissionDao.availableActionInUserId(actionId, userId);
    }
}
