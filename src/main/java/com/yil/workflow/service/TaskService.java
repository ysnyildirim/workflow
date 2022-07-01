package com.yil.workflow.service;

import com.yil.workflow.dto.TaskBaseRequest;
import com.yil.workflow.dto.TaskDto;
import com.yil.workflow.dto.TaskRequest;
import com.yil.workflow.dto.TaskResponse;
import com.yil.workflow.exception.*;
import com.yil.workflow.model.Task;
import com.yil.workflow.repository.TaskDao;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskDao taskDao;
    private final FlowService flowService;
    private final PriorityTypeService priorityTypeService;
    private final TaskActionService taskActionService;

    public static TaskDto toDto(Task task) throws NullPointerException {
        if (task == null)
            throw new NullPointerException("Task is null");
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setFinishDate(task.getFinishDate());
        dto.setEstimatedFinishDate(task.getEstimatedFinishDate());
        dto.setPriorityTypeId(task.getPriorityTypeId());
        dto.setStartDate(task.getStartDate());
        dto.setFlowId(task.getFlowId());
        return dto;
    }

    @Transactional(readOnly = true)
    public Page<Task> findAll(Pageable pageable) {
        return taskDao.findAll(pageable);
    }

    @Transactional(rollbackFor = {Throwable.class})
    public void delete(long id, long userId) throws YouDoNotHavePermissionException, TaskNotFoundException {
        if (!isDeletable(id, userId))
            throw new YouDoNotHavePermissionException();
        taskDao.deleteById(id);
    }

    public boolean isDeletable(long id, long userId) {
        return true;
    }

    @Transactional(rollbackFor = {Throwable.class})
    public TaskResponse replace(TaskBaseRequest request, long taskId, long userId) throws YouDoNotHavePermissionException, TaskNotFoundException {
        if (!isEditable(taskId, userId))
            throw new YouDoNotHavePermissionException();
        Task task = findById(taskId);
        task.setPriorityTypeId(request.getPriorityTypeId());
        task.setStartDate(request.getStartDate());
        task.setFinishDate(request.getFinishDate());
        task.setEstimatedFinishDate(request.getEstimatedFinishDate());
        task = taskDao.save(task);
        return TaskResponse
                .builder()
                .taskId(task.getId())
                .build();
    }

    public boolean isEditable(long id, long userId) {
        return true;
    }

    @Transactional(readOnly = true)
    public Task findById(Long id) throws TaskNotFoundException {
        return taskDao.findById(id).orElseThrow(() -> new TaskNotFoundException());
    }

    @Transactional(rollbackFor = {Throwable.class})
    public TaskResponse save(TaskRequest request, long userId) throws FlowNotFoundException, ActionNotFoundException, YouDoNotHavePermissionException, PriorityNotFoundException, NotAvailableActionException, StepNotFoundException {
        if (!flowService.existsByIdAndEnabledTrueAndDeletedTimeIsNull(request.getFlowId()))
            throw new FlowNotFoundException();
        if (!priorityTypeService.existsByIdAndDeletedTimeIsNull(request.getPriorityTypeId()))
            throw new PriorityNotFoundException();
        Task task = new Task();
        task.setFlowId(request.getFlowId());
        task.setPriorityTypeId(request.getPriorityTypeId());
        task.setStartDate(request.getStartDate());
        task.setFinishDate(request.getFinishDate());
        task.setEstimatedFinishDate(request.getEstimatedFinishDate());
        task = taskDao.save(task);

        taskActionService.save(request.getActionRequest(), task.getId(), userId);
        TaskResponse responce = new TaskResponse();
        responce.setTaskId(task.getId());
        return responce;
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long taskId) {
        return taskDao.existsById(taskId);
    }
}
