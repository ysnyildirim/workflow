package com.yil.workflow.service;

import com.yil.workflow.dto.ActionPermissionDto;
import com.yil.workflow.dto.ActionPermissionRequest;
import com.yil.workflow.dto.ActionPermissionResponse;
import com.yil.workflow.exception.ActionNotFoundException;
import com.yil.workflow.exception.ActionPermissionNotFoundException;
import com.yil.workflow.exception.ActionPermissionTypeNotFoundException;
import com.yil.workflow.model.ActionPermission;
import com.yil.workflow.repository.ActionDao;
import com.yil.workflow.repository.ActionPermissionDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ActionPermissionService {
    private final ActionPermissionDao actionPermissionDao;
    private final ActionPermissionTypeService actionPermissionTypeService;
    private final ActionDao actionDao;

    public static ActionPermissionDto toDto(ActionPermission actionPermission) {
        return ActionPermissionDto
                .builder()
                .id(actionPermission.getId())
                .actionId(actionPermission.getActionId())
                .actionPermissionTypeId(actionPermission.getActionPermissionTypeId())
                .permissionId(actionPermission.getPermissionId())
                .build();
    }

    public ActionPermissionResponse save(ActionPermissionRequest request, long actionId) throws ActionNotFoundException, ActionPermissionTypeNotFoundException {
        if (!actionDao.existsById(actionId))
            throw new ActionNotFoundException();
        if (!actionPermissionTypeService.existsById(request.getActionPermissionTypeId()))
            throw new ActionPermissionTypeNotFoundException();
        ActionPermission actionPermission = new ActionPermission();
        actionPermission.setActionId(actionId);
        actionPermission.setPermissionId(request.getPermissionId());
        actionPermission.setActionPermissionTypeId(request.getActionPermissionTypeId());
        actionPermission = actionPermissionDao.save(actionPermission);
        return ActionPermissionResponse.builder().id(actionPermission.getId()).build();
    }

    public void replace(ActionPermissionRequest request, int id) throws ActionPermissionNotFoundException {
        ActionPermission actionPermission = findById(id);
        actionPermission.setPermissionId(request.getPermissionId());
        actionPermission.setActionPermissionTypeId(request.getActionPermissionTypeId());
        actionPermissionDao.save(actionPermission);
    }

    public ActionPermission findById(int id) throws ActionPermissionNotFoundException {
        return actionPermissionDao.findById(id).orElseThrow(ActionPermissionNotFoundException::new);
    }

    public List<ActionPermission> findAllByActionId(long actionId) {
        return actionPermissionDao.findAllByActionId(actionId);
    }

    public void deleteById(int id) {
        actionPermissionDao.deleteById(id);
    }

    public boolean existsAllByActionIdAndActionPermissionTypeId(long actionId, int actionPermissionTypeId) {
        return actionPermissionDao.existsAllByActionIdAndActionPermissionTypeId(actionId, actionPermissionTypeId);
    }

    public List<ActionPermission> findAllByActionIdAndActionPermissionTypeId(long actionId, int actionPermissionTypeId) {
        return actionPermissionDao.findAllByActionIdAndActionPermissionTypeId(actionId, actionPermissionTypeId);
    }

    public boolean existsById(int id) {
        return actionPermissionDao.existsById(id);
    }
}
