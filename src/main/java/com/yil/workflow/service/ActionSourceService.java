/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.service;

import com.yil.workflow.dto.ActionSourceDto;
import com.yil.workflow.dto.ActionSourceRequest;
import com.yil.workflow.dto.ActionSourceResponse;
import com.yil.workflow.exception.ActionSourceNotFoundException;
import com.yil.workflow.exception.TargetNotFoundException;
import com.yil.workflow.model.ActionSource;
import com.yil.workflow.repository.ActionSourceDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ActionSourceService {

    private final ActionSourceDao actionSourceDao;
    private final TargetTypeService targetTypeService;

    public static ActionSourceDto toDto(ActionSource actionSource) {
        if (actionSource == null)
            throw new NullPointerException();
        ActionSourceDto dto = new ActionSourceDto();
        dto.setId(actionSource.getId());
        dto.setTargetTypeId(actionSource.getTargetTypeId());
        dto.setActionId(actionSource.getActionId());
        dto.setFlowGroupId(actionSource.getFlowGroupId());
        return dto;
    }

    public ActionSource findById(Long id) throws ActionSourceNotFoundException {
        return actionSourceDao.findById(id).orElseThrow(ActionSourceNotFoundException::new);
    }

    @Transactional
    public ActionSourceResponse save(ActionSourceRequest request, long actionId) throws TargetNotFoundException {
        ActionSource actionSource = new ActionSource();
        actionSource.setActionId(actionId);
        return getActionSourceResponse(request, actionSource);
    }

    private ActionSourceResponse getActionSourceResponse(ActionSourceRequest request, ActionSource actionSource) throws TargetNotFoundException {
        if (!targetTypeService.existsById(request.getTargetTypeId()))
            throw new TargetNotFoundException();
        actionSource.setTargetTypeId(request.getTargetTypeId());
        actionSource.setFlowGroupId(request.getFlowGroupId());
        actionSource = actionSourceDao.save(actionSource);
        return ActionSourceResponse.builder().id(actionSource.getId()).build();
    }

    @Transactional
    public ActionSourceResponse replace(ActionSourceRequest request, long actionSourceId) throws ActionSourceNotFoundException, TargetNotFoundException {
        ActionSource actionSource = findById(actionSourceId);
        return getActionSourceResponse(request, actionSource);
    }

    public List<ActionSource> findAllByActionIdAndTargetTypeId(long actionId, int targetTypeId) {
        return actionSourceDao.findAllByActionIdAndTargetTypeId(actionId, targetTypeId);
    }

    public List<ActionSource> findAllByActionId(long actionId) {
        return actionSourceDao.findAllByActionId(actionId);
    }

}
