package com.yil.workflow.controller;

import com.yil.workflow.base.ApiHeaders;
import com.yil.workflow.base.PageDto;
import com.yil.workflow.dto.CreateTaskActionMessageDto;
import com.yil.workflow.dto.TaskActionMessageDto;
import com.yil.workflow.model.TaskActionMessage;
import com.yil.workflow.service.TaskActionMessageService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping(value = "v1/task-actions/{taskActionId}/messages")
public class TaskActionMessageController {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final TaskActionMessageService taskActionMessageService;

    @Autowired
    public TaskActionMessageController(TaskActionMessageService taskActionMessageService) {
        this.taskActionMessageService = taskActionMessageService;
    }

    @GetMapping
    public ResponseEntity<PageDto<TaskActionMessageDto>> findAll(
            @PathVariable Long taskActionId,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "1000") int size) {
        try {
            if (page < 0)
                page = 0;
            if (size <= 0 || size > 1000)
                size = 1000;
            Pageable pageable = PageRequest.of(page, size);
            Page<TaskActionMessage> taskPage = taskActionMessageService.findAllByTaskActionIdAndDeletedTimeIsNull(pageable, taskActionId);
            PageDto<TaskActionMessageDto> pageDto = PageDto.toDto(taskPage, TaskActionMessageService::toDto);
            return ResponseEntity.ok(pageDto);
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<TaskActionMessageDto> findById(
            @PathVariable Long taskActionId,
            @PathVariable Long id) {
        try {
            TaskActionMessage task;
            try {
                task = taskActionMessageService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            if (!task.getTaskActionId().equals(taskActionId))
                return ResponseEntity.notFound().build();
            TaskActionMessageDto dto = TaskActionMessageService.toDto(task);
            return ResponseEntity.ok(dto);
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long taskActionId,
                                 @Valid @RequestBody CreateTaskActionMessageDto dto) {
        try {
            TaskActionMessage entity = new TaskActionMessage();
            entity.setTaskActionId(taskActionId);
            entity.setMessageId(dto.getMessageId());
            entity.setCreatedUserId(authenticatedUserId);
            entity.setCreatedTime(new Date());
            entity = taskActionMessageService.save(entity);
            return ResponseEntity.created(null).build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity replace(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                  @PathVariable Long taskActionId,
                                  @PathVariable Long id,
                                  @Valid @RequestBody CreateTaskActionMessageDto dto) {
        try {
            TaskActionMessage entity = null;
            try {
                entity = taskActionMessageService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            if (!entity.getTaskActionId().equals(taskActionId))
                return ResponseEntity.notFound().build();
            entity.setMessageId(dto.getMessageId());
            entity = taskActionMessageService.save(entity);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                         @PathVariable Long taskActionId,
                                         @PathVariable Long id) {
        try {
            TaskActionMessage entity;
            try {
                entity = taskActionMessageService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            if (!entity.getTaskActionId().equals(taskActionId))
                return ResponseEntity.notFound().build();
            entity.setDeletedUserId(authenticatedUserId);
            entity.setDeletedTime(new Date());
            entity = taskActionMessageService.save(entity);
            return ResponseEntity.ok("Action email deleted.");
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


}
