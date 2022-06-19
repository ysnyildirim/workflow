package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.PageDto;
import com.yil.workflow.dto.CreateTaskActionDto;
import com.yil.workflow.dto.TaskActionDto;
import com.yil.workflow.model.TaskAction;
import com.yil.workflow.service.TaskActionService;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/wf/v1/tasks/{taskId}/actions")
public class TaskActionController {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final TaskActionService taskActionService;

    @GetMapping
    public ResponseEntity<PageDto<TaskActionDto>> findAll(
            @PathVariable Long taskId,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) {
        try {
            if (page < 0)
                page = 0;
            if (size <= 0 || size > 1000)
                size = 1000;
            Pageable pageable = PageRequest.of(page, size);
            Page<TaskAction> taskPage = taskActionService.findAllByAndTaskIdAndDeletedTimeIsNull(pageable, taskId);
            PageDto<TaskActionDto> pageDto = PageDto.toDto(taskPage, TaskActionService::toDto);
            return ResponseEntity.ok(pageDto);
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<TaskActionDto> findById(
            @PathVariable Long taskId,
            @PathVariable Long id) {
        try {
            TaskAction task;
            try {
                task = taskActionService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            if (!task.getTaskId().equals(taskId))
                return ResponseEntity.notFound().build();
            TaskActionDto dto = TaskActionService.toDto(task);
            return ResponseEntity.ok(dto);
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long taskId,
                                 @Valid @RequestBody CreateTaskActionDto dto) {
        try {
            TaskAction entity = new TaskAction();
            entity.setTaskId(taskId);
            entity.setActionId(dto.getActionId());
            entity.setUserId(dto.getUserId());
            entity.setCreatedUserId(authenticatedUserId);
            entity.setCreatedTime(new Date());
            entity = taskActionService.save(entity);
            return ResponseEntity.created(null).build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                  @PathVariable Long taskId,
                                  @PathVariable Long id,
                                  @Valid @RequestBody CreateTaskActionDto dto) {
        try {
            TaskAction entity = null;
            try {
                entity = taskActionService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            if (!entity.getTaskId().equals(taskId))
                return ResponseEntity.notFound().build();
            entity.setTaskId(taskId);
            entity.setActionId(dto.getActionId());
            entity = taskActionService.save(entity);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                         @PathVariable Long taskId,
                                         @PathVariable Long id) {
        try {
            TaskAction entity;
            try {
                entity = taskActionService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            if (!entity.getActionId().equals(taskId))
                return ResponseEntity.notFound().build();
            entity.setDeletedUserId(authenticatedUserId);
            entity.setDeletedTime(new Date());
            entity = taskActionService.save(entity);
            return ResponseEntity.ok("Action email deleted.");
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


}
