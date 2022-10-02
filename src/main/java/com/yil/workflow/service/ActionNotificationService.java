/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */
package com.yil.workflow.service;

import com.yil.workflow.dto.ActionNotificationDto;
import com.yil.workflow.dto.ActionNotificationRequest;
import com.yil.workflow.dto.ActionNotificationResponse;
import com.yil.workflow.exception.FlowNotFoundException;
import com.yil.workflow.exception.StatusNotFoundException;
import com.yil.workflow.exception.StepTypeNotFoundException;
import com.yil.workflow.model.ActionNotification;
import com.yil.workflow.repository.ActionNotificationDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ActionNotificationService {
    private ActionNotificationDao actionNotificationDao;

    public static ActionNotificationDto convert(ActionNotification actionNotification) {
        ActionNotificationDto dto = ActionNotificationDto
                .builder()
                .actionId(actionNotification.getActionId())
                .message(actionNotification.getMessage())
                .subject(actionNotification.getSubject())
                .build();
        return dto;
    }

    @Transactional(rollbackFor = {Throwable.class})
    public ActionNotificationResponse save(ActionNotificationRequest request, Long actionId) throws FlowNotFoundException, StepTypeNotFoundException, StatusNotFoundException {
        ActionNotification notification = new ActionNotification();
        notification.setActionId(actionId);
        notification.setMessage(request.getMessage());
        notification.setSubject(request.getSubject());
        notification = actionNotificationDao.save(notification);
        return ActionNotificationResponse.builder().id(notification.getId()).build();
    }
}
