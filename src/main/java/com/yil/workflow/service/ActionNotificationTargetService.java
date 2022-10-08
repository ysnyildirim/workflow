/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.service;

import com.yil.workflow.dto.ActionNotificationTargetRequest;
import com.yil.workflow.dto.ActionNotificationTargetResponse;
import com.yil.workflow.model.ActionNotificationTarget;
import com.yil.workflow.repository.ActionNotificationTargetDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ActionNotificationTargetService {

    private final ActionNotificationTargetDao actionNotificationTargetDao;

    public ActionNotificationTargetResponse save(ActionNotificationTargetRequest request, Long actionNotificationId) {
        ActionNotificationTarget target = new ActionNotificationTarget();
        target.setActionNotificationId(actionNotificationId);
        target.setActionNotificationTargetTypeId(request.getActionNotificationTargetTypeId());
        target.setUserId(request.getUserId());
        target = actionNotificationTargetDao.save(target);
        return ActionNotificationTargetResponse.builder().id(target.getId()).build();
    }
}
