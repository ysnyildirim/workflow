package com.yil.workflow.service;

import com.yil.workflow.dto.TaskActionDocumentRequest;
import com.yil.workflow.dto.TaskActionDto;
import com.yil.workflow.dto.TaskActionMessageRequest;
import com.yil.workflow.dto.TaskActionRequest;
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
    private final TaskDao taskDao;
    private final AccountService accountService;
    private final ActionPermissionService actionPermissionService;

    public static TaskActionDto convert(TaskAction taskAction) {
        TaskActionDto dto = new TaskActionDto();
        dto.setId(taskAction.getId());
        dto.setTaskActionId(taskAction.getId());
        dto.setTaskId(taskAction.getTaskId());
        return dto;
    }

    @Transactional(rollbackFor = {Throwable.class})
    public TaskAction save(TaskActionRequest request, long taskId, long userId) throws ActionNotFoundException, YouDoNotHavePermissionException,
            StartUpActionException, NotNextActionException, StepNotFoundException, TaskActionNotFoundException {
        Action action = actionService.findByIdAndEnabledTrue(request.getActionId());
        TaskAction lastTaskAction = taskActionDao.getLastAction(taskId).orElse(null);
        Step step = stepService.findByIdAndEnabledTrue(action.getStepId());
        if (lastTaskAction == null) { // yeni ise başlangıç adımı mı ?
            if (!step.getStepTypeId().equals(StepTypeService.Start.getId()))
                throw new StartUpActionException();
        } else {
            if (!actionService.existsByIdAndNextStepId(lastTaskAction.getActionId(), step.getId()))
                throw new NotNextActionException();
        }
        if (!hasPermission(taskId, userId, lastTaskAction, action))
            throw new YouDoNotHavePermissionException();

        TaskAction taskAction = new TaskAction();
        taskAction.setTaskId(taskId);
        taskAction.setActionId(request.getActionId());
        taskAction.setParentId(lastTaskAction != null ? lastTaskAction.getId() : null);

        if (ActionTargetTypeService.Ozel.getId().equals(action.getActionTargetTypeId())) {
            taskAction.setAssignedUserId(request.getAssignedUserId());
        } else if (ActionTargetTypeService.BelirliBiri.getId().equals(action.getActionTargetTypeId())) {
            taskAction.setAssignedUserId(action.getNextUserId());
        } else if (ActionTargetTypeService.SonIslemYapan.getId().equals(action.getActionTargetTypeId())) {
            if (lastTaskAction == null)
                throw new TaskActionNotFoundException();
            taskAction.setAssignedUserId(lastTaskAction.getAssignedUserId());
        } else if (ActionTargetTypeService.IslemYapan.getId().equals(action.getActionTargetTypeId())) {
            taskAction.setAssignedUserId(userId);
        } else if (ActionTargetTypeService.Olusturan.getId().equals(action.getActionTargetTypeId())) {
            TaskAction firstAction = taskActionDao.getFirstAction(taskId).orElseThrow(TaskActionNotFoundException::new);
            taskAction.setAssignedUserId(firstAction.getCreatedUserId());
        } else if (ActionTargetTypeService.IslemYapanFarkliSonKisi.getId().equals(action.getActionTargetTypeId())) {
            TaskAction item = taskActionDao.getActionCreatedLastDifferentUser(taskId, userId).orElseThrow(TaskActionNotFoundException::new);
            taskAction.setAssignedUserId(item.getCreatedUserId());
        }

        taskAction.setCreatedUserId(userId);
        taskAction.setCreatedTime(new Date());
        taskAction = taskActionDao.save(taskAction);

        if (step.isCanAddDocument() && request.getDocuments() != null)
            for (TaskActionDocumentRequest doc : request.getDocuments())
                taskActionDocumentService.save(doc, taskAction.getId(), userId);

        if (step.isCanAddMessage() && request.getMessages() != null)
            for (TaskActionMessageRequest message : request.getMessages())
                taskActionMessageService.save(message, taskAction.getId(), userId);

        //Tamamlanma adımında kapatalım
        if (stepService.existsByIdAndStepTypeId(action.getNextStepId(), StepTypeService.Complete.getId())) {
            Task task = taskDao.findById(taskId).orElse(null);
            if (task != null) {
                task.setClosed(true);
                taskDao.save(task);
            }
        }
        return taskAction;
    }

    private boolean hasPermission(long taskId, long userId, TaskAction lastTaskAction, Action action) {
        if (lastTaskAction != null &&
            lastTaskAction.getAssignedUserId().equals(userId) &&
            actionPermissionService.existsById(ActionPermission.Pk.builder().actionId(action.getId()).actionPermissionTypeId(ActionPermissionTypeService.Atanan.getId()).build())) {
            return true;
        } else if (lastTaskAction != null &&
                   lastTaskAction.getCreatedUserId().equals(userId) &&
                   actionPermissionService.existsById(ActionPermission.Pk.builder().actionId(action.getId()).actionPermissionTypeId(ActionPermissionTypeService.SonIslemYapan.getId()).build())) {
            return true;
        } else if (actionPermissionService.existsById(ActionPermission.Pk.builder().actionId(action.getId()).actionPermissionTypeId(ActionPermissionTypeService.Herkes.getId()).build())) {
            return true;
        } else if (actionPermissionService.existsById(ActionPermission.Pk.builder().actionId(action.getId()).actionPermissionTypeId(ActionPermissionTypeService.Olusturan.getId()).build()) &&
                   taskActionDao.isTaskCreatedUser(taskId, userId)) {
            return true;
        } else if (actionPermissionService.existsById(ActionPermission.Pk.builder().actionId(action.getId()).actionPermissionTypeId(ActionPermissionTypeService.IslemYapanlar.getId()).build()) &&
                   taskActionDao.existsByTaskIdAndCreatedUserId(taskId, userId)) {
            return true;
        } else return action.getPermissionId() != null &&
                      actionPermissionService.existsById(ActionPermission.Pk.builder().actionId(action.getId()).actionPermissionTypeId(ActionPermissionTypeService.YetkisiOlan.getId()).build()) &&
                      accountService.existsPermission(action.getPermissionId(), userId);
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
    public boolean existsById(long id) {
        return taskActionDao.existsById(id);
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
    public List<Action> getNextActions(long taskId, long userId) throws TaskNotFoundException {
        List<Action> nextActions = new ArrayList<>();
        TaskAction lastTaskAction = null;
        try {
            lastTaskAction = getLastAction(taskId);
        } catch (TaskActionNotFoundException e) {
            throw new TaskNotFoundException();
        }
        List<Action> actions = actionService.getNextActions(lastTaskAction.getActionId());
        for (Action action : actions) {
            if (hasPermission(taskId, userId, lastTaskAction, action))
                nextActions.add(action);
        }
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
