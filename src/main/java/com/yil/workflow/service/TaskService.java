package com.yil.workflow.service;

import com.yil.workflow.dto.TaskBaseRequest;
import com.yil.workflow.dto.TaskDto;
import com.yil.workflow.dto.TaskRequest;
import com.yil.workflow.dto.TaskResponse;
import com.yil.workflow.exception.*;
import com.yil.workflow.model.Flow;
import com.yil.workflow.model.Task;
import com.yil.workflow.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;
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

    public Task findByIdAndDeletedTimeIsNull(Long id) throws TaskNotFoundException {
        return taskRepository.findByIdAndDeletedTimeIsNull(id).orElseThrow(() -> new TaskNotFoundException());
    }

    public Page<Task> findAllByDeletedTimeIsNull(Pageable pageable) {
        return taskRepository.findAllByDeletedTimeIsNull(pageable);
    }

    public boolean isEditable(long id, long userId) {
        return true;
    }

    public boolean isDeletable(long id, long userId) {
        return true;
    }

    @Transactional
    public void delete(long id, long userId) throws YouDoNotHavePermissionException, TaskNotFoundException {
        if (!isDeletable(id, userId))
            throw new YouDoNotHavePermissionException();
        Task task = findByIdAndDeletedTimeIsNull(id);
        task.setDeletedUserId(userId);
        task.setDeletedTime(new Date());
        task = taskRepository.save(task);
    }

    @Transactional
    public TaskResponse replace(TaskBaseRequest request, long taskId, long userId) throws YouDoNotHavePermissionException, TaskNotFoundException {
        if (!isEditable(taskId, userId))
            throw new YouDoNotHavePermissionException();
        Task task = findByIdAndDeletedTimeIsNull(taskId);
        task.setPriorityTypeId(request.getPriorityTypeId());
        task.setStartDate(request.getStartDate());
        task.setFinishDate(request.getFinishDate());
        task.setEstimatedFinishDate(request.getEstimatedFinishDate());
        task = taskRepository.save(task);
        return TaskResponse
                .builder()
                .taskId(task.getId())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    public TaskResponse save(TaskRequest request, long userId) throws FlowNotFoundException, ActionNotFoundException, YouDoNotHavePermissionException, PriorityNotFoundException, NotAvailableActionException, StepNotFoundException {
        Flow flow = flowService.findByIdAndEnabledTrueAndDeletedTimeIsNull(request.getFlowId());
        if (!priorityTypeService.existsByIdAndDeletedTimeIsNull(request.getPriorityTypeId()))
            throw new PriorityNotFoundException();
        Task task = new Task();
        task.setFlowId(flow.getId());
        task.setPriorityTypeId(request.getPriorityTypeId());
        task.setStartDate(request.getStartDate());
        task.setFinishDate(request.getFinishDate());
        task.setEstimatedFinishDate(request.getEstimatedFinishDate());
        task.setCreatedUserId(userId);
        task.setCreatedTime(new Date());
        task = taskRepository.save(task);

        taskActionService.save(request.getActionRequest(), task.getId(), userId);

        TaskResponse responce = new TaskResponse();
        responce.setTaskId(task.getId());
        return responce;
    }

    public boolean existsById(Long taskId) {
        return taskRepository.existsById(taskId);
    }
}
