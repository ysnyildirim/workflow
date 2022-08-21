package com.yil.workflow.service;

import com.yil.workflow.dto.TaskBaseRequest;
import com.yil.workflow.dto.TaskDto;
import com.yil.workflow.dto.TaskRequest;
import com.yil.workflow.dto.TaskResponse;
import com.yil.workflow.exception.*;
import com.yil.workflow.model.Task;
import com.yil.workflow.model.TaskAction;
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
    private final ActionService actionService;

    public static TaskDto convert(Task task) {
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setFinishDate(task.getFinishDate());
        dto.setEstimatedFinishDate(task.getEstimatedFinishDate());
        dto.setPriorityTypeId(task.getPriorityTypeId());
        dto.setStartDate(task.getStartDate());
        return dto;
    }

    @Transactional(readOnly = true)
    public Page<Task> findAll(Pageable pageable) {
        return taskDao.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Task> findAllByActionCreatedUserId(Pageable pageable, long userId) {
        return taskDao.findAllByActionCreatedUserId(pageable, userId);
    }

    @Transactional(readOnly = true)
    public Page<Task> findAllByActionCreatedUserIdAndClosed(Pageable pageable, long userId, boolean closed) {
        return taskDao.findAllByActionCreatedUserIdAndClosed(pageable, userId, (closed ? 1 : 0));
    }

    @Transactional(readOnly = true)
    public Page<Task> findAllByAssignedUserIdAndClosedFalse(Pageable pageable, long userId) {
        return taskDao.findAllByAssignedUserIdAndClosedFalse(pageable, userId);
    }

    @Transactional(readOnly = true)
    public Page<Task> findAllByCreatedUserId(Pageable pageable, long userId) {
        return taskDao.findAllByCreatedUserId(pageable, userId);
    }


    @Transactional(readOnly = true)
    public Page<Task> findAllByCreatedUserIdAndClosed(Pageable pageable, long userId, boolean closed) {
        return taskDao.findAllByCreatedUserIdAndClosed(pageable, userId, (closed ? 1 : 0));
    }

    @Transactional(rollbackFor = {Throwable.class})
    public TaskResponse replace(TaskBaseRequest request, long taskId, long userId) throws TaskNotFoundException, TaskActionNotFoundException, PriorityNotFoundException {
        TaskAction lastAction = taskActionService.getLastAction(taskId);
        if (!priorityTypeService.existsByIdAndDeletedTimeIsNull(request.getPriorityTypeId()))
            throw new PriorityNotFoundException();
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

    @Transactional(readOnly = true)
    public Task findById(Long id) throws TaskNotFoundException {
        return taskDao.findById(id).orElseThrow(() -> new TaskNotFoundException());
    }

    @Transactional(rollbackFor = {Throwable.class})
    public TaskResponse save(TaskRequest request, long userId) throws ActionNotFoundException, YouDoNotHavePermissionException, PriorityNotFoundException, StartUpActionException, NotNextActionException, StepNotFoundException, TaskActionNotFoundException {
        if (!priorityTypeService.existsByIdAndDeletedTimeIsNull(request.getPriorityTypeId()))
            throw new PriorityNotFoundException();
        Task task = new Task();
        task.setPriorityTypeId(request.getPriorityTypeId());
        task.setStartDate(request.getStartDate());
        task.setFinishDate(request.getFinishDate());
        task.setEstimatedFinishDate(request.getEstimatedFinishDate());
        task.setClosed(false);
        task = taskDao.save(task);
        taskActionService.save(request.getActionRequest(), task.getId(), userId);
        return TaskResponse
                .builder()
                .taskId(task.getId())
                .build();
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long taskId) {
        return taskDao.existsById(taskId);
    }

}
