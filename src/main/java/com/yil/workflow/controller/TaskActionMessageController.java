package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.PageDto;
import com.yil.workflow.dto.TaskActionMessageRequest;
import com.yil.workflow.dto.TaskActionMessageResponce;
import com.yil.workflow.dto.TaskActionMessageDto;
import com.yil.workflow.exception.TaskActionMessageNotFoundException;
import com.yil.workflow.exception.TaskActionNotFoundException;
import com.yil.workflow.model.TaskAction;
import com.yil.workflow.model.TaskActionMessage;
import com.yil.workflow.service.TaskActionMessageService;
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
@RequestMapping(value = "/api/wf/v1/task-actions/{taskActionId}/messages")
public class TaskActionMessageController {

    private final TaskActionMessageService taskActionMessageService;
    private final TaskActionService taskActionService;

    @GetMapping
    public ResponseEntity<PageDto<TaskActionMessageDto>> findAll(
            @PathVariable Long taskActionId,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        Pageable pageable = PageRequest.of(page, size);
        Page<TaskActionMessage> taskPage = taskActionMessageService.findAllByTaskActionIdAndDeletedTimeIsNull(pageable, taskActionId);
        PageDto<TaskActionMessageDto> pageDto = PageDto.toDto(taskPage, TaskActionMessageService::toDto);
        return ResponseEntity.ok(pageDto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TaskActionMessageDto> findById(
            @PathVariable Long taskActionId,
            @PathVariable Long id) throws TaskActionMessageNotFoundException {
        TaskActionMessage task = taskActionMessageService.findByIdAndTaskActionIdAndDeletedTimeIsNull(id, taskActionId);
        TaskActionMessageDto dto = TaskActionMessageService.toDto(task);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TaskActionMessageResponce> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                            @PathVariable Long taskActionId,
                                                            @Valid @RequestBody TaskActionMessageRequest request) throws TaskActionNotFoundException {
        TaskAction taskAction = taskActionService.findByIdAndDeletedTimeIsNull(taskActionId);
        TaskActionMessageResponce taskActionMessageResponce = taskActionMessageService.save(request, taskAction.getId(), authenticatedUserId);
        return ResponseEntity.created(null).body(taskActionMessageResponce);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                         @PathVariable Long taskActionId,
                                         @PathVariable Long id) throws TaskActionMessageNotFoundException {
        TaskActionMessage entity = taskActionMessageService.findByIdAndTaskActionIdAndDeletedTimeIsNull(id, taskActionId);
        entity.setDeletedUserId(authenticatedUserId);
        entity.setDeletedTime(new Date());
        entity = taskActionMessageService.save(entity);
        return ResponseEntity.ok("Message deleted.");
    }


}
