package com.yil.workflow.service;

import com.yil.workflow.dto.ActionDto;
import com.yil.workflow.dto.ActionRequest;
import com.yil.workflow.dto.ActionResponse;
import com.yil.workflow.exception.ActionNotFoundException;
import com.yil.workflow.exception.StepNotFoundException;
import com.yil.workflow.model.Action;
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

    public static ActionDto toDto(Action action) throws NullPointerException {
        if (action == null)
            throw new NullPointerException("Action is null");
        ActionDto dto = new ActionDto();
        dto.setId(action.getId());
        dto.setDescription(action.getDescription());
        dto.setEnabled(action.getEnabled());
        dto.setName(action.getName());
        dto.setStepId(action.getStepId());
        dto.setNextStepId(action.getNextStepId());
        return dto;
    }

    @Transactional(readOnly = true)
    public Action findById(Long id) throws ActionNotFoundException {
        return actionDao.findById(id).orElseThrow(() -> new ActionNotFoundException());
    }

    @Transactional(readOnly = true)
    public Action findByIdAndStepId(Long id, Long stepId) throws ActionNotFoundException {
        return actionDao.findByIdAndStepId(id, stepId).orElseThrow(() -> new ActionNotFoundException());
    }

    @Transactional(readOnly = true)
    public Action findByIdAndEnabledTrueAndDeletedTimeIsNull(Long id, Long stepId) throws ActionNotFoundException {
        return actionDao.findByIdAndEnabledTrueAndDeletedTimeIsNull(id).orElseThrow(() -> new ActionNotFoundException());
    }

    @Transactional(rollbackFor = {Throwable.class})
    public ActionResponse save(ActionRequest request, Long stepId, Long userId) throws StepNotFoundException {
        if (!stepService.existsById(stepId))
            throw new StepNotFoundException();
        Action action = new Action();
        action.setStepId(stepId);
        return getActionResponce(request, userId, action);
    }

    public ActionResponse getActionResponce(ActionRequest request, Long userId, Action action) throws StepNotFoundException {
        if (!stepService.existsById(request.getNextStepId()))
            throw new StepNotFoundException();
        action.setName(request.getName());
        action.setDescription(request.getDescription());
        action.setEnabled(request.getEnabled());
        action.setNextStepId(request.getNextStepId());
        action.setCreatedUserId(userId);
        action.setCreatedTime(new Date());
        action = actionDao.save(action);
        return ActionResponse.builder().id(action.getId()).build();
    }

    @Transactional(rollbackFor = {Throwable.class})
    public ActionResponse replace(ActionRequest request, Long actionId, Long userId) throws ActionNotFoundException, StepNotFoundException {
        Action action = findByIdAndEnabledTrueAndDeletedTimeIsNull(actionId);
        return getActionResponce(request, userId, action);
    }

    @Transactional(readOnly = true)
    public Action findByIdAndEnabledTrueAndDeletedTimeIsNull(Long id) throws ActionNotFoundException {
        return actionDao.findByIdAndEnabledTrueAndDeletedTimeIsNull(id).orElseThrow(() -> new ActionNotFoundException());
    }

    @Transactional(rollbackFor = {Throwable.class})
    public void delete(Long actionId, Long userId) throws ActionNotFoundException {
        Action action = findByIdAndDeletedTimeIsNull(actionId);
        action.setDeletedUserId(userId);
        action.setDeletedTime(new Date());
        actionDao.save(action);
    }

    @Transactional(readOnly = true)
    public Action findByIdAndDeletedTimeIsNull(Long id) throws ActionNotFoundException {
        return actionDao.findByIdAndDeletedTimeIsNull(id).orElseThrow(() -> new ActionNotFoundException());
    }

    @Transactional(readOnly = true)
    public List<Action> findAllByStepIdAndDeletedTimeIsNull(Long stepId) {
        return actionDao.findAllByStepIdAndDeletedTimeIsNull(stepId);
    }

    public List<ActionDto> findAll(long stepId) {
        List<Action> actionList = actionDao.findAllByStepIdAndDeletedTimeIsNull(stepId);
        List<ActionDto> actions = new ArrayList<>();
        actionList.forEach(f -> {
            actions.add(ActionService.toDto(f));
        });
        return actions;
    }

    @Transactional(readOnly = true)
    public boolean isStartUpAction(long id) {
        return actionDao.isStartUpAction(id);
    }

    @Transactional(readOnly = true)
    public boolean isNextAction(long currentActionId, long nextActionId) {
        return actionDao.isNextAction(currentActionId, nextActionId);
    }

    @Transactional(readOnly = true)
    public List<ActionDto> getActionByStepIdAndUserId(Long stepId, Long userId) {
        List<Action> actionList = actionDao.getActionByStepIdAndUserId(stepId, userId);
        List<ActionDto> actions = new ArrayList<>();
        actionList.forEach(f -> {
            actions.add(ActionService.toDto(f));
        });
        return actions;
    }

    @Transactional(readOnly = true)
    public List<ActionDto> getActionByStepIdAndTargetTypeId(long stepId, int targetTypeId) {
        List<Action> actionList = actionDao.getActionByStepIdAndTargetTypeId(stepId, targetTypeId);
        List<ActionDto> actions = new ArrayList<>();
        actionList.forEach(f -> {
            actions.add(ActionService.toDto(f));
        });
        return actions;
    }

    @Transactional(readOnly = true)
    public List<ActionDto> getStartUpActions(long flowId, long userId) {
        List<Action> actionList = actionDao.getStartUpActions(flowId, userId);
        List<ActionDto> actions = new ArrayList<>();
        actionList.forEach(f -> {
            actions.add(ActionService.toDto(f));
        });
        return actions;
    }
}
