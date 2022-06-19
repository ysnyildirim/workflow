package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.PageDto;
import com.yil.workflow.dto.CreateTaskActionDto;
import com.yil.workflow.dto.TaskActionDto;
import com.yil.workflow.exception.TaskActionNotFoundException;
import com.yil.workflow.model.TaskAction;
import com.yil.workflow.service.TaskActionService;
import lombok.RequiredArgsConstructor;
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
@RequestMapping(value = "/api/wf/v1/tasks/{taskId}/actions")
public class TaskActionController {

    private final TaskActionService taskActionService;

    @GetMapping
    public ResponseEntity<PageDto<TaskActionDto>> findAll(
            @PathVariable Long taskId,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        Pageable pageable = PageRequest.of(page, size);
        Page<TaskAction> taskPage = taskActionService.findAllByAndTaskIdAndDeletedTimeIsNull(pageable, taskId);
        PageDto<TaskActionDto> pageDto = PageDto.toDto(taskPage, TaskActionService::toDto);
        return ResponseEntity.ok(pageDto);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<TaskActionDto> findByIdAndTaskIdAndDeletedTimeIsNull(
            @PathVariable Long taskId,
            @PathVariable Long id) throws TaskActionNotFoundException {
        TaskAction task = taskActionService.findByIdAndTaskIdAndDeletedTimeIsNull(id, taskId);
        TaskActionDto dto = TaskActionService.toDto(task);
        return ResponseEntity.ok(dto);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TaskActionDto> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                @PathVariable Long taskId,
                                                @Valid @RequestBody CreateTaskActionDto request) {
        TaskAction entity = new TaskAction();
        entity.setTaskId(taskId);
        entity.setActionId(request.getActionId());
        entity.setUserId(request.getUserId());
        entity.setCreatedUserId(authenticatedUserId);
        entity.setCreatedTime(new Date());
        entity = taskActionService.save(entity);
        TaskActionDto dto = TaskActionService.toDto(entity);
        return ResponseEntity.created(null).body(dto);
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                  @PathVariable Long taskId,
                                  @PathVariable Long id,
                                  @Valid @RequestBody CreateTaskActionDto request) throws TaskActionNotFoundException {
        TaskAction entity = taskActionService.findByIdAndTaskIdAndDeletedTimeIsNull(id, taskId);
        entity.setTaskId(taskId);
        entity.setActionId(request.getActionId());
        entity = taskActionService.save(entity);
        TaskActionDto dto = TaskActionService.toDto(entity);
        return ResponseEntity.ok(entity);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                         @PathVariable Long taskId,
                                         @PathVariable Long id) throws TaskActionNotFoundException {
        TaskAction entity = taskActionService.findByIdAndTaskIdAndDeletedTimeIsNull(id, taskId);
        entity.setDeletedUserId(authenticatedUserId);
        entity.setDeletedTime(new Date());
        entity = taskActionService.save(entity);
        return ResponseEntity.ok("Action email deleted.");
    }


}
