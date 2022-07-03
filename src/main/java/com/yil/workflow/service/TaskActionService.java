package com.yil.workflow.service;

import com.yil.workflow.dto.*;
import com.yil.workflow.exception.*;
import com.yil.workflow.model.*;
import com.yil.workflow.repository.TaskActionDao;
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
    private final ActionService actionService;
    private final TaskActionDocumentService taskActionDocumentService;
    private final TaskActionMessageService taskActionMessageService;
    private final StepService stepService;
    private final FlowService flowService;
    private final GroupUserService groupUserService;
    private final TaskDao taskDao;

    public static TaskActionResponse toDto(TaskAction taskAction) throws NullPointerException {
        if (taskAction == null)
            throw new NullPointerException("TaskAction is null");
        TaskActionResponse dto = new TaskActionResponse();
        dto.setId(taskAction.getId());
        dto.setActionId(taskAction.getActionId());
        dto.setTaskId(taskAction.getTaskId());
        return dto;
    }

    @Transactional(rollbackFor = {Throwable.class})
    public TaskActionResponse save(TaskActionRequest request, long taskId, long userId) throws ActionNotFoundException, NotAvailableActionException, YouDoNotHavePermissionException, StepNotFoundException, StartUpActionException, NotNextActionException {
        TaskAction lastAction = taskActionDao.getLastAction(taskId).orElse(null);
        //region action control
        if (lastAction == null) { // yeni ise başlangıç aksiyonu mu ?
            if (!actionService.isStartUpAction(request.getActionId()))
                throw new StartUpActionException();
        } else {
            if (!actionService.isNextAction(lastAction.getActionId(), request.getActionId()))
                throw new NotNextActionException();
        }
        //endregion action control

        //region permission control
        Action action = actionService.findByIdAndEnabledTrueAndDeletedTimeIsNull(request.getActionId());
        switch (action.getTargetTypeId()) {
            case TargetTypeService.User:
                if (!action.getUserId().equals(userId))
                    throw new YouDoNotHavePermissionException();
                break;
            case TargetTypeService.GroupMembers:
                if (!groupUserService.isGroupUser(action.getGroupId(), userId))
                    throw new YouDoNotHavePermissionException();
                break;
            case TargetTypeService.TaskCreator:
                TaskAction firstAction = null;
                if (lastAction != null && lastAction.getParentId() != null) //current action parent id is null then first action
                    firstAction = taskActionDao.getFirstAction(taskId).orElse(null);
                else
                    firstAction = lastAction;
                if (firstAction == null || !firstAction.getCreatedUserId().equals(userId))
                    throw new YouDoNotHavePermissionException();
                break;
            case TargetTypeService.LastActionUser:
                if (lastAction == null || !lastAction.getCreatedUserId().equals(userId))
                    throw new YouDoNotHavePermissionException();
                break;
            default:
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

        if (request.getDocuments() != null)
            for (TaskActionDocumentRequest doc : request.getDocuments()) {
                taskActionDocumentService.save(doc, taskAction.getId(), userId);
            }

        if (request.getMessages() != null)
            for (TaskActionMessageRequest message : request.getMessages()) {
                taskActionMessageService.save(message, taskAction.getId(), userId);
            }

        if (action.getAssignable()) {
            Long assignUserId = null;
            if (Arrays.asList(TargetTypeService.User,
                    TargetTypeService.LastActionUser,
                    TargetTypeService.TaskCreator).contains(action.getTargetTypeId()))
                assignUserId = userId;
            else if (action.getTargetTypeId().equals(TargetTypeService.GroupMembers)) {
                List<GroupUser> managers = groupUserService.getManagers(action.getGroupId());
                if (managers != null && !managers.isEmpty())
                    assignUserId = managers.get(0).getUserId();
            }
            Task task = taskDao.findById(taskId).orElse(null);
            if (task != null) {
                task.setAssignedUserId(assignUserId);
                taskDao.save(task);
            }
        }

        return TaskActionResponse
                .builder()
                .id(taskAction.getId())
                .build();
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
    public List<ActionDto> getNextActions(long taskId, long userId) throws ActionNotFoundException, StepNotFoundException, TaskNotFoundException, TaskActionNotFoundException {
        List<ActionDto> actions = new ArrayList<>();
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
            List<ActionDto> lastActionUserActions = actionService.findAllByStepIdAndTargetTypeIdAndEnabledTrueAndDeletedTimeIsNull(step.getId(), TargetTypeService.LastActionUser);
            if (lastActionUserActions != null)
                actions.addAll(lastActionUserActions);
        }
        List<ActionDto> groupActions = actionService.getGroupActionsByStepIdAndUserId(step.getId(), userId);
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
