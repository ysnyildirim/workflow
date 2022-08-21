package com.yil.workflow.service;

import com.yil.workflow.model.ActionPermission;
import com.yil.workflow.repository.ActionPermissionDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActionPermissionService {
    private final ActionPermissionDao actionPermissionDao;

    public boolean existsById(ActionPermission.Pk id) {
        return actionPermissionDao.existsById(id);
    }

    @Transactional(rollbackFor = {Throwable.class})
    public ActionPermission save(ActionPermission actionPermission) {
        return actionPermissionDao.save(actionPermission);
    }

    public List<ActionPermission> findAllByActionId(Long actionId) {
        return actionPermissionDao.findAllById_ActionId(actionId);
    }

    public void delete(ActionPermission.Pk pk) {
        actionPermissionDao.deleteById(pk);
    }
}
