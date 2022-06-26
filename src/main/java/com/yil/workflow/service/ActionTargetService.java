/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.service;

import com.yil.workflow.dto.ActionTargetDto;
import com.yil.workflow.model.ActionTarget;
import com.yil.workflow.repository.ActionTargetDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ActionTargetService {

    private final ActionTargetDao actionTargetDao;

    public static ActionTargetDto toDto(ActionTarget actionTarget) {
        if (actionTarget == null)
            throw new NullPointerException();
        ActionTargetDto dto = new ActionTargetDto();
        dto.setId(actionTarget.getId());
        dto.setTargetId(actionTarget.getTargetId());
        dto.setActionId(actionTarget.getActionId());
        dto.setGroupId(actionTarget.getFlowGroupId());
        return dto;
    }

}
