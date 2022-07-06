package com.yil.workflow.service;

import com.yil.workflow.dto.TaskActionDocumentRequest;
import com.yil.workflow.dto.TaskActionDto;
import com.yil.workflow.dto.TaskActionMessageRequest;
import com.yil.workflow.dto.TaskActionRequest;
import com.yil.workflow.exception.*;
import com.yil.workflow.model.Action;
import com.yil.workflow.model.Step;
import com.yil.workflow.model.TaskAction;
import com.yil.workflow.model.TaskActionTarget;
import com.yil.workflow.repository.TaskActionDao;
import com.yil.workflow.repository.TaskActionTargetDao;
import com.yil.workflow.repository.TaskDao;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskActionService {

    private final TaskActionDao taskActionDao;
    private final TaskActionTargetDao taskActionTargetDao;
    private final ActionService actionService;
    private final TaskActionDocumentService taskActionDocumentService;
    private final TaskActionMessageService taskActionMessageService;
    private final StepService stepService;
    private final FlowService flowService;
    private final GroupUserService groupUserService;
    private final GroupService groupService;
    private final TaskDao taskDao;
    private final ActionPermissionService actionPermissionService;

    public static TaskActionDto convert(TaskAction taskAction) {
        TaskActionDto dto = new TaskActionDto();
        dto.setId(taskAction.getId());
        dto.setActionId(taskAction.getActionId());
        dto.setTaskId(taskAction.getTaskId());
        return dto;
    }

    @Transactional(rollbackFor = {Throwable.class})
    public TaskAction save(TaskActionRequest request, long taskId, long userId) throws ActionNotFoundException, YouDoNotHavePermissionException, StartUpActionException, NotNextActionException, GroupNotFoundException, TargetGroupNotHavePermissionException, TargetUserNotHavePermissionException {
        TaskAction lastAction = taskActionDao.getLastAction(taskId).orElse(null);
        TaskAction firstAction = null;
        //region action control
        if (lastAction == null) { // yeni ise başlangıç aksiyonu mu ?
            if (!actionService.isStartUpAction(request.getActionId()))
                throw new StartUpActionException();
        } else {
            if (!actionService.isNextAction(lastAction.getActionId(), request.getActionId()))
                throw new NotNextActionException();
            if (lastAction.getParentId() != null)
                firstAction = taskActionDao.getFirstAction(taskId).orElse(null);
            else
                firstAction = lastAction;
        }
        //endregion action control

        //region permission control
        Action action = actionService.findByIdAndEnabledTrueAndDeletedTimeIsNull(request.getActionId());
        {
            boolean permission = false;
            //user yetkisi varsa
            if (actionPermissionService.existsByActionIdAndTargetTypeIdAndUserId(action.getId(), TargetTypeService.User, userId))
                permission = true;
            else if (actionPermissionService.existsByActionIdAndTargetTypeId(action.getId(), TargetTypeService.TaskCreator)) {
                if (firstAction != null && firstAction.getCreatedUserId().equals(userId)) {
                    permission = true;
                }
            } else if (actionPermissionService.existsByActionIdAndTargetTypeId(action.getId(), TargetTypeService.GroupMembers)) {
                if (actionPermissionService.availableActionInUserId(action.getId(), userId))
                    permission = true;
            }
            if (!permission)
                throw new YouDoNotHavePermissionException();
        }
        //endregion permission control

        TaskAction taskAction = new TaskAction();
        taskAction.setTaskId(taskId);
        taskAction.setActionId(request.getActionId());
        taskAction.setParentId(lastAction != null ? lastAction.getId() : null);
        taskAction.setCreatedUserId(userId);
        taskAction.setCreatedTime(new Date());
        taskAction = taskActionDao.save(taskAction);

        if (request.getTargetGroups() != null) {
            List<Long> targetGroups = new ArrayList<>();
            Arrays.stream(request.getTargetGroups()).forEach(f -> {
                if (!targetGroups.contains(f))
                    targetGroups.add(f);
            });
            for (Long targetGroupId : targetGroups) {
                if (!actionPermissionService.existsByActionIdAndTargetTypeIdAndGroupId(action.getId(), TargetTypeService.GroupMembers, targetGroupId))
                    throw new TargetGroupNotHavePermissionException();
                TaskActionTarget taskActionTarget = new TaskActionTarget();
                taskActionTarget.setGroupId(targetGroupId);
                taskActionTarget.setActionId(taskAction.getActionId());
                taskActionTargetDao.save(taskActionTarget);
            }
        }
        if (request.getTargetUsers() != null && request.getTargetUsers().length > 0) {
            List<Long> targetUsers = new ArrayList<>();
            Arrays.stream(request.getTargetUsers()).forEach(f -> {
                if (!targetUsers.contains(f))
                    targetUsers.add(f);
            });
            boolean createdUserAvailable = actionPermissionService.existsByActionIdAndTargetTypeId(action.getId(), TargetTypeService.TaskCreator);
            boolean lastUserAvailable = actionPermissionService.existsByActionIdAndTargetTypeId(action.getId(), TargetTypeService.LastActionUser);
            for (Long targetUserId : targetUsers) {
                boolean state = false;
                if (createdUserAvailable && firstAction != null && firstAction.getCreatedUserId().equals(targetUserId))
                    state = true;
                else if (lastUserAvailable && lastAction != null && lastAction.getCreatedUserId().equals(targetUserId))
                    state = true;
                else if (actionPermissionService.existsByActionIdAndTargetTypeIdAndUserId(action.getId(), TargetTypeService.User, targetUserId))
                    state = true;
                if (!state)
                    throw new TargetUserNotHavePermissionException();
                TaskActionTarget taskActionTarget = new TaskActionTarget();
                taskActionTarget.setUserId(targetUserId);
                taskActionTarget.setActionId(taskAction.getActionId());
                taskActionTargetDao.save(taskActionTarget);
            }
        }

        if (request.getDocuments() != null)
            for (TaskActionDocumentRequest doc : request.getDocuments()) {
                taskActionDocumentService.save(doc, taskAction.getId(), userId);
            }

        if (request.getMessages() != null)
            for (TaskActionMessageRequest message : request.getMessages()) {
                taskActionMessageService.save(message, taskAction.getId(), userId);
            }
        return taskAction;
    }

    /**
     * Sadece aksiyonu icra eden silebilir
     *
     * @param taskActionId
     * @param userId
     * @throws YouDoNotHavePermissionException
     */
    @Transactional(rollbackFor = {Throwable.class})
    public void delete(long taskActionId, long userId) throws YouDoNotHavePermissionException {
        if (!taskActionDao.existsByIdAndCreatedUserId(taskActionId, userId))
            throw new YouDoNotHavePermissionException();
        taskActionDao.deleteById(taskActionId);
    }

    @Transactional(readOnly = true)
    public TaskAction findById(Long id) throws TaskActionNotFoundException {
        return taskActionDao.findById(id).orElseThrow(() -> new TaskActionNotFoundException());
    }

    @Transactional(readOnly = true)
    public Page<TaskAction> findAllByTaskId(Pageable pageable, Long taskId) {
        return taskActionDao.findAllByTaskId(pageable, taskId);
    }

    @Transactional(readOnly = true)
    public TaskAction findByIdAndTaskId(Long id, Long taskId) throws TaskActionNotFoundException {
        return taskActionDao.findByIdAndTaskId(id, taskId).orElseThrow(() -> new TaskActionNotFoundException());
    }

    @Transactional(readOnly = true)
    public List<Action> getNextActions(long taskId, long userId) throws ActionNotFoundException, StepNotFoundException, TaskNotFoundException, TaskActionNotFoundException {
        List<Action> actions = new ArrayList<>();
        TaskAction lastAction = null;
        try {
            lastAction = getLastAction(taskId);
        } catch (TaskActionNotFoundException e) {
            throw new TaskNotFoundException();
        }
        Action action = actionService.findById(lastAction.getActionId());
        if (action == null)
            return actions;
        Step step = stepService.findByIdAndEnabledTrueAndDeletedTimeIsNull(action.getNextStepId());
        if (step == null)
            return actions;
        if (!flowService.existsByIdAndEnabledTrueAndDeletedTimeIsNull(step.getFlowId()))
            return actions;

        TaskAction firstAction = null;
        if (lastAction.getParentId() == null)
            firstAction = lastAction;
        else
            firstAction = getFirstAction(taskId);
        if (firstAction.getCreatedUserId().equals(userId))
            actions = actionService.findAllByStepIdAndTargetTypeIdAndEnabledTrueAndDeletedTimeIsNull(step.getId(), TargetTypeService.TaskCreator);
        if (lastAction.getCreatedUserId().equals(userId)) {
            List<Action> lastActionUserActions = actionService.findAllByStepIdAndTargetTypeIdAndEnabledTrueAndDeletedTimeIsNull(step.getId(), TargetTypeService.LastActionUser);
            if (lastActionUserActions != null)
                actions.addAll(lastActionUserActions);
        }
        List<Action> groupActions = actionService.getGroupActionsByStepIdAndUserId(step.getId(), userId);
        if (groupActions != null)
            actions.addAll(groupActions);
        return actions;
    }

    @Transactional(readOnly = true)
    public TaskAction getLastAction(long taskId) throws TaskActionNotFoundException {
        return taskActionDao.getLastAction(taskId).orElseThrow(() -> new TaskActionNotFoundException());
    }

    @Transactional(readOnly = true)
    public TaskAction getFirstAction(long taskId) throws TaskActionNotFoundException {
        return taskActionDao.getFirstAction(taskId).orElseThrow(() -> new TaskActionNotFoundException());
    }


}
