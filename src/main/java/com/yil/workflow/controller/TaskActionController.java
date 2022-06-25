package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.PageDto;
import com.yil.workflow.dto.TaskActionRequest;
import com.yil.workflow.dto.TaskActionResponce;
import com.yil.workflow.exception.*;
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

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/wf/v1/tasks/{taskId}/actions")
public class TaskActionController {

    private final TaskActionService taskActionService;

    @GetMapping
    public ResponseEntity<PageDto<TaskActionResponce>> findAll(
            @PathVariable Long taskId,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        Pageable pageable = PageRequest.of(page, size);
        Page<TaskAction> taskPage = taskActionService.findAllByTaskIdAndDeletedTimeIsNull(pageable, taskId);
        PageDto<TaskActionResponce> pageDto = PageDto.toDto(taskPage, TaskActionService::toDto);
        return ResponseEntity.ok(pageDto);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<TaskActionResponce> findByIdAndTaskIdAndDeletedTimeIsNull(
            @PathVariable Long taskId,
            @PathVariable Long id) throws TaskActionNotFoundException {
        TaskAction task = taskActionService.findByIdAndTaskIdAndDeletedTimeIsNull(id, taskId);
        TaskActionResponce dto = TaskActionService.toDto(task);
        return ResponseEntity.ok(dto);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TaskActionResponce> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                     @PathVariable Long taskId,
                                                     @Valid @RequestBody TaskActionRequest request) throws ActionNotFoundException, NotAvailableActionException, YouDoNotHavePermissionException {
        TaskActionResponce responce = taskActionService.save(request, taskId, authenticatedUserId);
        return ResponseEntity.created(null).body(responce);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                         @PathVariable Long taskId,
                                         @PathVariable Long id) throws TaskActionNotFoundException, YouDoNotHavePermissionException {
        taskActionService.delete(id, authenticatedUserId);
        return ResponseEntity.ok("Action deleted.");
    }


}
