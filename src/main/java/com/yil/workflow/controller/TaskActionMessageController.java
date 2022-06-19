package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.PageDto;
import com.yil.workflow.dto.CreateTaskActionMessageDto;
import com.yil.workflow.dto.TaskActionMessageDto;
import com.yil.workflow.exception.TaskActionMessageNotFoundException;
import com.yil.workflow.model.TaskActionMessage;
import com.yil.workflow.service.TaskActionMessageService;
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
@RequestMapping(value = "/api/wf/v1/task-actions/{taskActionId}/messages")
public class TaskActionMessageController {

    private final TaskActionMessageService taskActionMessageService;

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
    public ResponseEntity<TaskActionMessageDto> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                       @PathVariable Long taskActionId,
                                                       @Valid @RequestBody CreateTaskActionMessageDto request) {
        TaskActionMessage entity = new TaskActionMessage();
        entity.setTaskActionId(taskActionId);
        entity.setSubject(request.getSubject());
        entity.setContent(request.getContent());
        entity.setCreatedUserId(authenticatedUserId);
        entity.setCreatedTime(new Date());
        entity = taskActionMessageService.save(entity);
        TaskActionMessageDto dto = TaskActionMessageService.toDto(entity);
        return ResponseEntity.created(null).body(dto);
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                  @PathVariable Long taskActionId,
                                  @PathVariable Long id,
                                  @Valid @RequestBody CreateTaskActionMessageDto request) throws TaskActionMessageNotFoundException {
        TaskActionMessage entity = taskActionMessageService.findByIdAndTaskActionIdAndDeletedTimeIsNull(id, taskActionId);
        entity.setSubject(request.getSubject());
        entity.setContent(request.getContent());
        entity = taskActionMessageService.save(entity);
        TaskActionMessageDto dto = TaskActionMessageService.toDto(entity);
        return ResponseEntity.ok(dto);
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
