package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.PageDto;
import com.yil.workflow.dto.CreateTaskDto;
import com.yil.workflow.dto.TaskDto;
import com.yil.workflow.exception.TaskNotFoundException;
import com.yil.workflow.model.Task;
import com.yil.workflow.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/wf/v1/tasks")
public class TaskController {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<PageDto<TaskDto>> findAll(
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        Pageable pageable = PageRequest.of(page, size);
        Page<Task> taskPage = taskService.findAllByDeletedTimeIsNull(pageable);
        PageDto<TaskDto> pageDto = PageDto.toDto(taskPage, TaskService::toDto);
        return ResponseEntity.ok(pageDto);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<TaskDto> findByIdAndDeletedTimeIsNull(@PathVariable Long id) throws TaskNotFoundException {
        Task task = taskService.findByIdAndDeletedTimeIsNull(id);
        TaskDto dto = TaskService.toDto(task);
        return ResponseEntity.ok(dto);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TaskDto> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                          @Valid @RequestBody CreateTaskDto request) {
        Task task = new Task();
        task.setFlowId(request.getFlowId());
        task.setStatusId(request.getStatusId());
        task.setPriorityId(request.getPriorityId());
        task.setCurrentTaskActionId(request.getCurrentTaskActionId());
        task.setStartDate(request.getStartDate());
        task.setFinishDate(request.getFinishDate());
        task.setEstimatedFinishDate(request.getEstimatedFinishDate());
        task.setCreatedUserId(authenticatedUserId);
        task.setCreatedTime(new Date());
        task = taskService.save(task);
        TaskDto dto = TaskService.toDto(task);
        return ResponseEntity.created(null).body(dto);
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                  @PathVariable Long id,
                                  @Valid @RequestBody CreateTaskDto request) throws TaskNotFoundException {
        Task task = taskService.findByIdAndDeletedTimeIsNull(id);
        task.setFlowId(request.getFlowId());
        task.setStatusId(request.getStatusId());
        task.setPriorityId(request.getPriorityId());
        task.setCurrentTaskActionId(request.getCurrentTaskActionId());
        task.setStartDate(request.getStartDate());
        task.setFinishDate(request.getFinishDate());
        task.setEstimatedFinishDate(request.getEstimatedFinishDate());
        task = taskService.save(task);
        TaskDto dto = TaskService.toDto(task);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                         @PathVariable Long id) throws TaskNotFoundException {
        Task task = taskService.findByIdAndDeletedTimeIsNull(id);
        task.setDeletedUserId(authenticatedUserId);
        task.setDeletedTime(new Date());
        task = taskService.save(task);
        return ResponseEntity.ok("Task deleted.");
    }


}
