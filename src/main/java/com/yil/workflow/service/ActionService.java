package com.yil.workflow.service;

import com.yil.workflow.dto.ActionDto;
import com.yil.workflow.dto.ActionRequest;
import com.yil.workflow.dto.ActionResponse;
import com.yil.workflow.exception.*;
import com.yil.workflow.model.Action;
import com.yil.workflow.model.ActionPermission;
import com.yil.workflow.model.Step;
import com.yil.workflow.repository.ActionDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ActionService {
    private final ActionDao actionDao;
    private final StepService stepService;
    private final AccountService accountService;
    private final ActionPermissionService actionPermissionService;
    private final ActionTargetTypeService actionTargetTypeService;

    public static ActionDto convert(Action entity) {
        ActionDto dto = new ActionDto();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setEnabled(entity.isEnabled());
        dto.setName(entity.getName());
        dto.setStepId(entity.getStepId());
        dto.setNextStepId(entity.getNextStepId());
        dto.setPermissionId(entity.getPermissionId());
        return dto;
    }

    @Transactional(readOnly = true)
    public Action findById(Long id) throws ActionNotFoundException {
        return actionDao.findById(id).orElseThrow(ActionNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Action findByIdAndStepId(Long id, Long stepId) throws ActionNotFoundException {
        return actionDao.findByIdAndStepId(id, stepId).orElseThrow(ActionNotFoundException::new);
    }

    @Transactional(rollbackFor = {Throwable.class})
    public ActionResponse save(ActionRequest request, Long stepId, Long userId) throws StepNotFoundException, CannotBeAddedToThisStepException, ActionPermissionTypeNotFoundException, ActionTargetTypeNotFoundException {
        Step step = stepService.findById(stepId);
        if (step.getStepTypeId().equals(StepTypeService.Complete.getId()))
            throw new CannotBeAddedToThisStepException();
        Action action = new Action();
        action.setStepId(stepId);
        return getActionResponce(request, userId, action);
    }

    public ActionResponse getActionResponce(ActionRequest request, Long userId, Action action) throws StepNotFoundException, ActionTargetTypeNotFoundException {
        if (!stepService.existsById(request.getNextStepId()))
            throw new StepNotFoundException();
        if (!actionTargetTypeService.existsById(request.getActionTargetTypeId()))
            throw new ActionTargetTypeNotFoundException();
        action.setName(request.getName());
        action.setDescription(request.getDescription());
        action.setEnabled(request.getEnabled());
        action.setNextStepId(request.getNextStepId());
        action.setPermissionId(request.getPermissionId());
        action.setActionTargetTypeId(request.getActionTargetTypeId());
        if (ActionTargetTypeService.BelirliBiri.getId().equals(request.getActionTargetTypeId())) {
            action.setNextUserId(request.getNextUserId());
        } else
            action.setNextUserId(null);
        action.setCreatedUserId(userId);
        action.setCreatedTime(new Date());
        action = actionDao.save(action);
        return ActionResponse.builder().id(action.getId()).build();
    }

    @Transactional(rollbackFor = {Throwable.class})
    public ActionResponse replace(ActionRequest request, Long actionId, Long userId) throws ActionNotFoundException, StepNotFoundException, ActionTargetTypeNotFoundException {
        Action action = findByIdAndEnabledTrue(actionId);
        return getActionResponce(request, userId, action);
    }

    @Transactional(readOnly = true)
    public Action findByIdAndEnabledTrue(Long id) throws ActionNotFoundException {
        return actionDao.findByIdAndEnabledTrue(id).orElseThrow(ActionNotFoundException::new);
    }

    @Transactional(rollbackFor = {Throwable.class})
    public void delete(Long actionId, Long userId) {
        actionDao.deleteById(actionId);
    }

    public List<Action> findAll(long stepId) {
        return actionDao.findAllByStepId(stepId);
    }

    @Transactional(readOnly = true)
    boolean existsByIdAndNextStepId(long id, long nextStepId) {
        return actionDao.existsByIdAndNextStepId(id, nextStepId);
    }

    @Transactional(readOnly = true)
    public List<Action> getNextActions(long id) {
        return actionDao.getNextActions(id);
    }

    public List<Action> getStartActions(long flowId, long userId) {
        List<Action> availableActions = new ArrayList<>();
        List<Step> stepList = stepService.findAllByFlowIdAndStepTypeIdAndEnabledTrueAndDeletedTimeIsNull(flowId, StepTypeService.Start.getId());
        for (Step step : stepList) {
            List<Action> actions = findAllByStepIdAndEnabledTrue(step.getId());
            for (Action action : actions) {
                if (actionPermissionService.existsById(ActionPermission.Pk.builder().actionId(action.getId()).actionPermissionTypeId(ActionPermissionTypeService.Herkes.getId()).build())) {
                    availableActions.add(action);
                } else if (action.getPermissionId() != null &&
                           actionPermissionService.existsById(ActionPermission.Pk.builder().actionId(action.getId()).actionPermissionTypeId(ActionPermissionTypeService.YetkisiOlan.getId()).build()) &&
                           accountService.existsPermission(action.getPermissionId(), userId)) {
                    availableActions.add(action);
                }
            }
        }
        return availableActions;
    }

    @Transactional(readOnly = true)
    public List<Action> findAllByStepIdAndEnabledTrue(Long stepId) {
        return actionDao.findAllByStepIdAndEnabledTrue(stepId);
    }

    public boolean existsById(Long actionId) {
        return actionDao.existsById(actionId);
    }
}
