/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */
package com.yil.workflow.service;

import com.yil.workflow.dto.ActionNotificationDto;
import com.yil.workflow.dto.ActionNotificationRequest;
import com.yil.workflow.dto.ActionNotificationResponse;
import com.yil.workflow.exception.ActionNotificationNotFoundException;
import com.yil.workflow.exception.FlowNotFoundException;
import com.yil.workflow.exception.StatusNotFoundException;
import com.yil.workflow.exception.StepTypeNotFoundException;
import com.yil.workflow.model.ActionNotification;
import com.yil.workflow.repository.ActionNotificationDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActionNotificationService {
    private final  ActionNotificationDao actionNotificationDao;

    public static ActionNotificationDto toDto(ActionNotification actionNotification) {
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

    @Transactional(readOnly = true)
    public List<ActionNotification> findAllByActionId(Long actionId) {
        return actionNotificationDao.findAllByActionId(actionId);
    }

    @Transactional(rollbackFor = {Throwable.class})
    public void deleteById(Long id) {
        actionNotificationDao.deleteById(id);
    }

    @Transactional(rollbackFor = {Throwable.class})
    public void replace( ActionNotificationRequest request, Long id) throws ActionNotificationNotFoundException {
        ActionNotification actionNotification = findById(id);
        actionNotification.setMessage(request.getMessage());
        actionNotification.setSubject(request.getSubject());
        actionNotificationDao.save(actionNotification);
    }

    @Transactional(readOnly = true)
    public ActionNotification findById(Long id) throws ActionNotificationNotFoundException {
        return actionNotificationDao.findById(id).orElseThrow(ActionNotificationNotFoundException::new);
    }
}
