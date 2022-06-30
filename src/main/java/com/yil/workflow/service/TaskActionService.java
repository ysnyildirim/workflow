package com.yil.workflow.service;

import com.yil.workflow.dto.TaskActionDocumentRequest;
import com.yil.workflow.dto.TaskActionMessageRequest;
import com.yil.workflow.dto.TaskActionRequest;
import com.yil.workflow.dto.TaskActionResponse;
import com.yil.workflow.exception.*;
import com.yil.workflow.model.Action;
import com.yil.workflow.model.ActionSource;
import com.yil.workflow.model.TaskAction;
import com.yil.workflow.repository.TaskActionDao;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public static TaskActionResponse toDto(TaskAction taskAction) throws NullPointerException {
        if (taskAction == null)
            throw new NullPointerException("TaskAction is null");
        TaskActionResponse dto = new TaskActionResponse();
        dto.setId(taskAction.getId());
        dto.setActionId(taskAction.getActionId());
        dto.setTaskId(taskAction.getTaskId());
        return dto;
    }

    public TaskAction getLastAction(long taskId) {
        return taskActionDao.getLastAction(taskId);
    }

    private final ActionSourceService actionSourceService;
    private final FlowGroupUserService flowGroupUserService;

    public TaskAction findByTaskIdOrderByIdAsc(long taskId) throws TaskActionNotFoundException {
        return taskActionDao.findByTaskIdOrderByIdAsc(taskId).orElseThrow(() -> new TaskActionNotFoundException());
    }

    @Transactional
    public TaskActionResponse save(TaskActionRequest request, long taskId, long userId) throws ActionNotFoundException, NotAvailableActionException, YouDoNotHavePermissionException, StepNotFoundException {
        TaskAction currentTaskAction = getLastAction(taskId);

        //region permission control
        if (currentTaskAction == null) {
            boolean state = false;
            List<ActionSource> actionSources = actionSourceService.findAllByActionIdAndTargetTypeId(request.getActionId(), 3);
            for (ActionSource actionSource : actionSources) {
                if (flowGroupUserService.existsById(actionSource.getFlowGroupId(), userId)) {
                    state = true;
                    break;
                }
            }
            if (!state)
                throw new YouDoNotHavePermissionException();
        } else {
            TaskAction firstAction = null;
            try {
                if (currentTaskAction.getParentId() == null) //current action parent id is null then first action
                    firstAction = findByTaskIdOrderByIdAsc(taskId);
                else
                    firstAction = currentTaskAction;
            } catch (Exception e) {

            }
            boolean state = false;
            List<ActionSource> actionSources = actionSourceService.findAllByActionId(request.getActionId());
            for (ActionSource actionSource : actionSources) {
                if (actionSource.getTargetTypeId().equals(1)) { // task creator
                    if (firstAction != null && firstAction.getCreatedUserId().equals(userId)) { // first task creator is user ?
                        state = true;
                        break;
                    }
                } else if (actionSource.getTargetTypeId().equals(2)) { //last action user
                    if (currentTaskAction.getCreatedUserId().equals(userId)) // last action is user ?
                    {
                        state = true;
                        break;
                    }
                } else if (actionSource.getTargetTypeId().equals(3)) {
                    if (flowGroupUserService.existsById(actionSource.getFlowGroupId(), userId)) {
                        state = true;
                        break;
                    }
                }
            }
            if (!state)
                throw new YouDoNotHavePermissionException();
        }
        //endregion permission control

        Action action;

        //region action control
        if (currentTaskAction == null) { // yeni ise başlangıç aksiyonu mu ?
            action = actionService.findByIdAndDeletedTimeIsNull(request.getActionId());
            if (!stepService.existsByIdAndStepTypeIdAndEnabledTrueAndDeletedTimeIsNull(action.getStepId(), 1)) //action stepi aktif ve başlangıç step mi ?
                throw new StepNotFoundException();
        } else {
            Action currentAction = actionService.findByIdAndDeletedTimeIsNull(currentTaskAction.getActionId()); //sonraki adım doğrumu ?
            action = actionService.findByIdAndEnabledTrueAndDeletedTimeIsNull(request.getActionId(), currentAction.getNextStepId());
        }
        //endregion action control

        TaskAction taskAction = new TaskAction();
        taskAction.setTaskId(taskId);
        taskAction.setActionId(action.getId());
        taskAction.setParentId(currentTaskAction != null ? currentTaskAction.getId() : null);
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

        return TaskActionResponse
                .builder()
                .id(taskAction.getId())
                .build();
    }

    @Transactional
    public void delete(long taskActionId, long userId) throws YouDoNotHavePermissionException {
        taskActionDao.deleteById(taskActionId);
    }

    public TaskAction findById(Long id) throws TaskActionNotFoundException {
        return taskActionDao.findById(id).orElseThrow(() -> new TaskActionNotFoundException());
    }

    public Page<TaskAction> findAllByTaskId(Pageable pageable, Long taskId) {
        return taskActionDao.findAllByTaskId(pageable, taskId);
    }

    public TaskAction findByIdAndTaskId(Long id, Long taskId) throws TaskActionNotFoundException {
        return taskActionDao.findByIdAndTaskId(id, taskId).orElseThrow(() -> new TaskActionNotFoundException());
    }

}
