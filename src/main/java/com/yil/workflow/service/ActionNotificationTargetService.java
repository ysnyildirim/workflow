/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.service;

import com.yil.workflow.dto.ActionNotificationTargetDto;
import com.yil.workflow.dto.ActionNotificationTargetRequest;
import com.yil.workflow.dto.ActionNotificationTargetResponse;
import com.yil.workflow.exception.ActionNotificationTargetNotFoundException;
import com.yil.workflow.model.ActionNotificationTarget;
import com.yil.workflow.repository.ActionNotificationTargetDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ActionNotificationTargetService {

    private final ActionNotificationTargetDao actionNotificationTargetDao;

    public static ActionNotificationTargetDto toDto(ActionNotificationTarget actionNotificationTarget) {
        return ActionNotificationTargetDto
                .builder()
                .id(actionNotificationTarget.getId())
                .userId(actionNotificationTarget.getUserId())
                .actionNotificationId(actionNotificationTarget.getActionNotificationId())
                .actionNotificationTargetTypeId(actionNotificationTarget.getActionNotificationTargetTypeId())
                .build();
    }

    public ActionNotificationTargetResponse save(ActionNotificationTargetRequest request, Long actionNotificationId) {
        ActionNotificationTarget target = new ActionNotificationTarget();
        target.setActionNotificationId(actionNotificationId);
        target.setActionNotificationTargetTypeId(request.getActionNotificationTargetTypeId());
        target.setUserId(request.getUserId());
        target = actionNotificationTargetDao.save(target);
        return ActionNotificationTargetResponse.builder().id(target.getId()).build();
    }

    public List<ActionNotificationTarget> findAllByActionNotificationId(Long actionNotificationId) {
        return actionNotificationTargetDao.findAllByActionNotificationId(actionNotificationId);
    }

    public void deleteById(Long id) {
        actionNotificationTargetDao.deleteById(id);
    }

    public void replace(ActionNotificationTargetRequest request, Long id) throws ActionNotificationTargetNotFoundException {
        ActionNotificationTarget actionNotificationTarget = findById(id);
        actionNotificationTarget.setActionNotificationTargetTypeId(request.getActionNotificationTargetTypeId());
        actionNotificationTarget.setUserId(request.getUserId());
        actionNotificationTargetDao.save(actionNotificationTarget);
    }

    public ActionNotificationTarget findById(long id) throws ActionNotificationTargetNotFoundException {
        return actionNotificationTargetDao.findById(id).orElseThrow(ActionNotificationTargetNotFoundException::new);
    }
}
