package com.yil.workflow.service;

import com.yil.workflow.dto.*;
import com.yil.workflow.exception.*;
import com.yil.workflow.model.TaskAction;
import com.yil.workflow.repository.TaskActionDao;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class TaskActionService {

    private final TaskActionDao taskActionDao;
    private final ActionService actionService;
    private final TaskActionDocumentService taskActionDocumentService;
    private final TaskActionMessageService taskActionMessageService;
    private final ActionSourceService actionSourceService;

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
        TaskAction currentTaskAction = getLastAction(taskId);

        //region permission control
        if (currentTaskAction == null) {

            if (!actionSourceService.userInActionGroup(request.getActionId(), userId))
                throw new YouDoNotHavePermissionException();
        } else {
            TaskAction firstAction = null;
            if (currentTaskAction.getParentId() == null) //current action parent id is null then first action
                firstAction = taskActionDao.findByTaskIdOrderByIdAsc(taskId).orElse(null);
            else
                firstAction = currentTaskAction;
            boolean state = false;
            if (firstAction != null &&
                    firstAction.getCreatedUserId() != null &&
                    actionSourceService.existsByActionIdAndTargetTypeId(request.getActionId(), TargetTypeService.TaskCreator)) {
                state = true;
            } else if (currentTaskAction.getCreatedUserId() != null &&
                    actionSourceService.existsByActionIdAndTargetTypeId(request.getActionId(), TargetTypeService.LastActionUser)) {
                state = true;
            } else if (actionSourceService.userInActionGroup(request.getActionId(), userId)) {
                state = true;
            }
            if (!state)
                throw new YouDoNotHavePermissionException();
        }
        //endregion permission control

        //region action control
        if (currentTaskAction == null && !actionService.isStartUpAction(request.getActionId())) { // yeni ise başlangıç aksiyonu mu ?
            throw new StartUpActionException();
        } else {
            if (!actionService.isNextAction(currentTaskAction.getActionId(), request.getActionId()))
                throw new NotNextActionException();
        }
        //endregion action control

        TaskAction taskAction = new TaskAction();
        taskAction.setTaskId(taskId);
        taskAction.setActionId(request.getActionId());
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

    @Transactional(readOnly = true)
    public TaskAction getLastAction(long taskId) {
        return taskActionDao.getLastAction(taskId);
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

}
