package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.PageDto;
import com.yil.workflow.dto.*;
import com.yil.workflow.exception.*;
import com.yil.workflow.model.Task;
import com.yil.workflow.service.ActionService;
import com.yil.workflow.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/wf/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping
    public ResponseEntity<PageDto<TaskDto>> findAll(
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        Pageable pageable = PageRequest.of(page, size);
        Page<Task> taskPage = taskService.findAll(pageable);
        PageDto<TaskDto> pageDto = PageDto.toDto(taskPage, TaskService::toDto);
        return ResponseEntity.ok(pageDto);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "/{id}")
    public ResponseEntity<TaskDto> findByIdAndDeletedTimeIsNull(@PathVariable Long id) throws TaskNotFoundException {
        Task task = taskService.findById(id);
        TaskDto dto = TaskService.toDto(task);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TaskResponse> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                               @Valid @RequestBody TaskRequest request) throws FlowNotFoundException, ActionNotFoundException, PriorityNotFoundException, YouDoNotHavePermissionException, NotAvailableActionException, TaskNotFoundException, TaskActionNotFoundException, StepNotFoundException, StartUpActionException, NotNextActionException {
        TaskResponse responce = taskService.save(request, authenticatedUserId);
        return ResponseEntity.created(null).body(responce);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TaskResponse> replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                @PathVariable Long id,
                                                @Valid @RequestBody TaskBaseRequest request) throws TaskNotFoundException, YouDoNotHavePermissionException {
        TaskResponse responce = taskService.replace(request, id, authenticatedUserId);
        return ResponseEntity.ok(responce);
    }



}
