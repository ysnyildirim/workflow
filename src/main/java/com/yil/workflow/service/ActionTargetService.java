/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.service;

import com.yil.workflow.dto.ActionTargetDto;
import com.yil.workflow.dto.ActionTargetRequest;
import com.yil.workflow.dto.ActionTargetResponse;
import com.yil.workflow.exception.ActionTargetNotFoundException;
import com.yil.workflow.exception.TargetNotFoundException;
import com.yil.workflow.model.ActionTarget;
import com.yil.workflow.repository.ActionTargetDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ActionTargetService {

    private final ActionTargetDao actionTargetDao;
    private final TargetTypeService targetTypeService;

    public static ActionTargetDto toDto(ActionTarget actionTarget) {
        if (actionTarget == null)
            throw new NullPointerException();
        ActionTargetDto dto = new ActionTargetDto();
        dto.setId(actionTarget.getId());
        dto.setTargetTypeId(actionTarget.getTargetTypeId());
        dto.setActionId(actionTarget.getActionId());
        dto.setGroupId(actionTarget.getGroupId());
        return dto;
    }

    @Transactional(rollbackFor = {Throwable.class})
    public ActionTargetResponse save(ActionTargetRequest request, long actionId) throws TargetNotFoundException {
        ActionTarget actionTarget = new ActionTarget();
        actionTarget.setActionId(actionId);
        return getActionTargetResponse(request, actionTarget);
    }

    private ActionTargetResponse getActionTargetResponse(ActionTargetRequest request, ActionTarget actionTarget) throws TargetNotFoundException {
        if (!targetTypeService.existsById(request.getTargetTypeId()))
            throw new TargetNotFoundException();
        actionTarget.setTargetTypeId(request.getTargetTypeId());
        actionTarget.setGroupId(request.getGroupId());
        actionTarget = actionTargetDao.save(actionTarget);
        return ActionTargetResponse.builder().id(actionTarget.getId()).build();
    }

    @Transactional(rollbackFor = {Throwable.class})
    public ActionTargetResponse replace(ActionTargetRequest request, long actionTargetId) throws ActionTargetNotFoundException, TargetNotFoundException {
        ActionTarget actionTarget = findById(actionTargetId);
        return getActionTargetResponse(request, actionTarget);
    }

    @Transactional(readOnly = true)
    public ActionTarget findById(Long id) throws ActionTargetNotFoundException {
        return actionTargetDao.findById(id).orElseThrow(ActionTargetNotFoundException::new);
    }

}