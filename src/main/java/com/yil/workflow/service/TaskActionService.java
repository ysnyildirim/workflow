package com.yil.workflow.service;

import com.yil.workflow.dto.*;
import com.yil.workflow.exception.*;
import com.yil.workflow.model.Action;
import com.yil.workflow.model.Step;
import com.yil.workflow.model.TaskAction;
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
    private final TaskDao taskDao;
    private final AccountService accountService;

    public static TaskActionDto convert(TaskAction taskAction) {
        TaskActionDto dto = new TaskActionDto();
        dto.setId(taskAction.getId());
        dto.setActionId(taskAction.getActionId());
        dto.setTaskId(taskAction.getTaskId());
        return dto;
    }

    @Transactional(rollbackFor = {Throwable.class})
    public TaskAction save(TaskActionRequest request, long taskId, long userId) throws ActionNotFoundException, YouDoNotHavePermissionException, StartUpActionException, NotNextActionException, GroupNotFoundException, TargetGroupNotHavePermissionException, TargetUserNotHavePermissionException, StepNotFoundException {
        Action action = actionService.findByIdAndEnabledTrueAndDeletedTimeIsNull(request.getActionId());
        if (!accountService.existsPermission(action.getPermissionId(), userId))
            throw new YouDoNotHavePermissionException();
        TaskAction lastTaskAction = taskActionDao.getLastAction(taskId).orElse(null);
        Step step = stepService.findByIdAndEnabledTrueAndDeletedTimeIsNull(action.getStepId());
        if (lastTaskAction == null) { // yeni ise başlangıç aksiyonu mu ?
            if (!Arrays.asList(1, 2).contains(step.getStepTypeId()))
                throw new StartUpActionException();
        } else {
            Action lastAction = actionService.findById(lastTaskAction.getActionId());
            Step lastStep = stepService.findById(lastAction.getStepId());
            if (!step.getId().equals(lastAction.getNextStepId()))
                throw new NotNextActionException();
        }
        TaskAction taskAction = new TaskAction();
        taskAction.setTaskId(taskId);
        taskAction.setActionId(request.getActionId());
        taskAction.setParentId(lastTaskAction != null ? lastTaskAction.getId() : null);
        taskAction.setAssignedUserId(request.getAssignedUserId());
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

        //kapanıs
        Step nextStep = stepService.findById(action.getNextStepId());
        if (Arrays.asList(3, 4, 5).contains(nextStep.getStepTypeId())) {
            taskDao.closedTask(taskId);
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
    public List<ActionDto> getNextActions(long taskId, long userId) throws ActionNotFoundException, StepNotFoundException, TaskNotFoundException, TaskActionNotFoundException {
        List<ActionDto> nextActions = new ArrayList<>();
        TaskAction lastAction = null;
        try {
            lastAction = getLastAction(taskId);
        } catch (TaskActionNotFoundException e) {
            throw new TaskNotFoundException();
        }
        Action action = actionService.findById(lastAction.getActionId());
        if (action == null)
            return nextActions;
        Step step = stepService.findByIdAndEnabledTrueAndDeletedTimeIsNull(action.getNextStepId());
        if (step == null)
            return nextActions;
        if (!flowService.existsByIdAndEnabledTrueAndDeletedTimeIsNull(step.getFlowId()))
            return nextActions;
        List<Action> actions = actionService.findAllByStepIdAndEnabledTrueAndDeletedTimeIsNull(step.getId());
        for (Action item : actions)
            nextActions.add(ActionService.convert(item));
        return nextActions;
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
