package com.yil.workflow.service;

import com.yil.workflow.dto.TaskBaseRequest;
import com.yil.workflow.dto.TaskDto;
import com.yil.workflow.dto.TaskRequest;
import com.yil.workflow.dto.TaskResponce;
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
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final FlowService flowService;
    private final PriorityService priorityService;
    private final TaskActionService taskActionService;

    public static TaskDto toDto(Task task) throws NullPointerException {
        if (task == null)
            throw new NullPointerException("Task is null");
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setFinishDate(task.getFinishDate());
        dto.setEstimatedFinishDate(task.getEstimatedFinishDate());
        dto.setPriorityId(task.getPriorityId());
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

    public void delete(long id, long userId) throws YouDoNotHavePermissionException, TaskNotFoundException {
        if (!isDeletable(id, userId))
            throw new YouDoNotHavePermissionException();
        Task task = findByIdAndDeletedTimeIsNull(id);
        task.setDeletedUserId(userId);
        task.setDeletedTime(new Date());
        task = taskRepository.save(task);
    }

    public TaskResponce save(TaskBaseRequest request, long id, long userId) throws YouDoNotHavePermissionException, TaskNotFoundException {
        if (!isEditable(id, userId))
            throw new YouDoNotHavePermissionException();
        Task task = findByIdAndDeletedTimeIsNull(id);
        task.setPriorityId(request.getPriorityId());
        task.setStartDate(request.getStartDate());
        task.setFinishDate(request.getFinishDate());
        task.setEstimatedFinishDate(request.getEstimatedFinishDate());
        task = taskRepository.save(task);
        return TaskResponce
                .builder()
                .taskId(task.getId())
                .build();
    }

    public TaskResponce save(TaskRequest request, long userId) throws FlowNotFoundException, ActionNotFoundException, YouDoNotHavePermissionException, PriorityNotFoundException, NotAvailableActionException {
        Flow flow = flowService.findByIdAndEnabledTrueAndDeletedTimeIsNull(request.getFlowId());
        if (!priorityService.existsByIdAndDeletedTimeIsNull(request.getPriorityId()))
            throw new PriorityNotFoundException();
        Task task = new Task();
        task.setFlowId(flow.getId());
        task.setPriorityId(request.getPriorityId());
        task.setStartDate(request.getStartDate());
        task.setFinishDate(request.getFinishDate());
        task.setEstimatedFinishDate(request.getEstimatedFinishDate());
        task.setCreatedUserId(userId);
        task.setCreatedTime(new Date());
        task = taskRepository.save(task);

        taskActionService.save(request.getActionRequest(), task.getId(), userId);

        TaskResponce responce = new TaskResponce();
        responce.setTaskId(task.getId());
        return responce;
    }

}
