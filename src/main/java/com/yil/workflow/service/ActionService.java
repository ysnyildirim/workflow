package com.yil.workflow.service;

import com.yil.workflow.dto.ActionDto;
import com.yil.workflow.dto.ActionRequest;
import com.yil.workflow.dto.ActionResponse;
import com.yil.workflow.exception.*;
import com.yil.workflow.model.Action;
import com.yil.workflow.repository.ActionDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@RequiredArgsConstructor
@Service
public class ActionService {

    private final ActionDao actionDao;
    private final StepService stepService;
    private final GroupService groupService;
    private final TargetTypeService targetTypeService;


    public static ActionDto convert(Action entity) {
        ActionDto dto = new ActionDto();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setEnabled(entity.getEnabled());
        dto.setName(entity.getName());
        dto.setStepId(entity.getStepId());
        dto.setNextStepId(entity.getNextStepId());
        return dto;
    }

    @Transactional(readOnly = true)
    public boolean taskCanChangedByActionIdAndUserId(long id, long userId) {
        return actionDao.taskCanChangedByActionIdAndUserId(id, userId);
    }

    @Transactional(readOnly = true)
    public List<Action> findAllByStepIdAndTargetTypeIdAndEnabledTrueAndDeletedTimeIsNull(Long stepId, int targetTypeId) {
        return actionDao.findAllByStepIdAndTargetTypeIdAndEnabledTrueAndDeletedTimeIsNull(stepId, targetTypeId);
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
    public ActionResponse save(ActionRequest request, Long stepId, Long userId) throws StepNotFoundException, TargetNotFoundException, GroupNotFoundException, UserNotFoundException {
        if (!stepService.existsById(stepId))
            throw new StepNotFoundException();
        Action action = new Action();
        action.setStepId(stepId);
        return getActionResponce(request, userId, action);
    }

    public ActionResponse getActionResponce(ActionRequest request, Long userId, Action action) throws StepNotFoundException, TargetNotFoundException, GroupNotFoundException, UserNotFoundException {
        if (!stepService.existsById(request.getNextStepId()))
            throw new StepNotFoundException();
        if (!targetTypeService.existsById(request.getTargetTypeId()))
            throw new TargetNotFoundException();
        if (request.getTargetTypeId().equals(TargetTypeService.User))
            if (request.getUserId() == null)
                throw new UserNotFoundException();
        if (request.getTargetTypeId().equals(TargetTypeService.GroupMembers))
            if (!groupService.existsById(request.getGroupId()))
                throw new GroupNotFoundException();
        action.setName(request.getName());
        action.setDescription(request.getDescription());
        action.setEnabled(request.getEnabled());
        action.setNextStepId(request.getNextStepId());
        action.setGroupId(request.getGroupId());
        action.setUserId(request.getUserId());
        action.setTargetTypeId(request.getTargetTypeId());
        action.setCreatedUserId(userId);
        action.setCreatedTime(new Date());
        action = actionDao.save(action);
        return ActionResponse.builder().id(action.getId()).build();
    }

    @Transactional(rollbackFor = {Throwable.class})
    public ActionResponse replace(ActionRequest request, Long actionId, Long userId) throws ActionNotFoundException, StepNotFoundException, TargetNotFoundException, GroupNotFoundException, UserNotFoundException {
        Action action = findByIdAndEnabledTrueAndDeletedTimeIsNull(actionId);
        return getActionResponce(request, userId, action);
    }

    @Transactional(readOnly = true)
    public Action findByIdAndEnabledTrueAndDeletedTimeIsNull(Long id) throws ActionNotFoundException {
        return actionDao.findByIdAndEnabledTrueAndDeletedTimeIsNull(id).orElseThrow(ActionNotFoundException::new);
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
        return actionDao.findByIdAndDeletedTimeIsNull(id).orElseThrow(ActionNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<Action> findAllByStepIdAndDeletedTimeIsNull(Long stepId) {
        return actionDao.findAllByStepIdAndDeletedTimeIsNull(stepId);
    }

    public List<Action> findAll(long stepId) {
        return actionDao.findAllByStepIdAndDeletedTimeIsNull(stepId);
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
    public List<Action> getStartUpActions(long flowId, long userId) {
        return actionDao.getStartUpActions(flowId, userId);
    }

    @Transactional(readOnly = true)
    public List<Action> getGroupActionsByStepIdAndUserId(long stepId, long userId) {
        return actionDao.getGroupActionsByStepIdAndUserId(stepId, userId);
    }


}
