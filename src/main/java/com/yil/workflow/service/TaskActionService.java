package com.yil.workflow.service;

import com.yil.workflow.dto.TaskActionDocumentRequest;
import com.yil.workflow.dto.TaskActionMessageRequest;
import com.yil.workflow.dto.TaskActionRequest;
import com.yil.workflow.dto.TaskActionResponce;
import com.yil.workflow.exception.*;
import com.yil.workflow.model.Action;
import com.yil.workflow.model.TaskAction;
import com.yil.workflow.repository.TaskActionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class TaskActionService {

    private final TaskActionRepository taskActionRepository;
    private final ActionService actionService;
    private final TaskActionDocumentService taskActionDocumentService;
    private final TaskActionMessageService taskActionMessageService;
    private final StepService stepService;

    public TaskAction getLastAction(long taskId) {
        return taskActionRepository.getLastAction(taskId);
    }

    @Transactional
    public TaskActionResponce save(TaskActionRequest request, long taskId, long userId) throws ActionNotFoundException, NotAvailableActionException, YouDoNotHavePermissionException, StepNotFoundException {
        if (!isTaskActionCreatable(taskId, userId))
            throw new YouDoNotHavePermissionException();
        TaskAction currentTaskAction = getLastAction(taskId);
        Action action;
        if (currentTaskAction == null) { // yeni ise başlangıç aksiyonu mu ?
            action = actionService.findByIdAndDeletedTimeIsNull(request.getActionId());
            if (!stepService.existsByIdAndStepTypeIdAndEnabledTrueAndDeletedTimeIsNull(action.getStepId(), 1))
                throw new StepNotFoundException();
        } else {
            Action currentAction = actionService.findById(currentTaskAction.getActionId());
            action = actionService.findByIdAndStepIdAndEnabledTrueAndDeletedTimeIsNotNull(request.getActionId(), currentAction.getNextStepId());
        }
        //bu action yapabilme yetkisi varmı ?
        if (!actionService.availableAction(action.getId(), userId))
            throw new NotAvailableActionException();

        TaskAction taskAction = new TaskAction();
        taskAction.setTaskId(taskId);
        taskAction.setActionId(action.getId());
        taskAction.setCreatedUserId(userId);
        taskAction.setCreatedTime(new Date());
        taskAction = taskActionRepository.save(taskAction);

        if (request.getDocuments() != null)
            for (TaskActionDocumentRequest doc : request.getDocuments()) {
                taskActionDocumentService.save(doc, taskAction.getId(), userId);
            }

        if (request.getMessages() != null)
            for (TaskActionMessageRequest message : request.getMessages()) {
                taskActionMessageService.save(message, taskAction.getId(), userId);
            }

        return TaskActionResponce
                .builder()
                .id(taskAction.getId())
                .build();
    }

    @Transactional
    public void delete(long taskActionId, long userId) throws YouDoNotHavePermissionException, TaskActionNotFoundException {
        if (!isTaskActionDeletable(taskActionId, userId))
            throw new YouDoNotHavePermissionException();
        TaskAction entity = findByIdAndDeletedTimeIsNull(taskActionId);
        entity.setDeletedUserId(userId);
        entity.setDeletedTime(new Date());
        entity = taskActionRepository.save(entity);
    }

    public static TaskActionResponce toDto(TaskAction taskAction) throws NullPointerException {
        if (taskAction == null)
            throw new NullPointerException("TaskAction is null");
        TaskActionResponce dto = new TaskActionResponce();
        dto.setId(taskAction.getId());
        dto.setActionId(taskAction.getActionId());
        dto.setTaskId(taskAction.getTaskId());
        return dto;
    }

    public TaskAction findByIdAndDeletedTimeIsNull(Long id) throws TaskActionNotFoundException {
        return taskActionRepository.findByIdAndDeletedTimeIsNull(id).orElseThrow(() -> new TaskActionNotFoundException());
    }

    public Page<TaskAction> findAllByTaskIdAndDeletedTimeIsNull(Pageable pageable, Long taskId) {
        return taskActionRepository.findAllByTaskIdAndDeletedTimeIsNull(pageable, taskId);
    }

    public TaskAction findByIdAndTaskIdAndDeletedTimeIsNull(Long id, Long taskId) throws TaskActionNotFoundException {
        return taskActionRepository.findByIdAndTaskIdAndDeletedTimeIsNull(id, taskId).orElseThrow(() -> new TaskActionNotFoundException());
    }

    public boolean isTaskActionCreatable(long taskId, long userId) {
        return true;
    }

    public boolean isTaskActionDeletable(long id, long userId) {
        return true;
    }

}
