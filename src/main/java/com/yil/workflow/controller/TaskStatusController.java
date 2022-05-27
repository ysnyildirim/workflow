package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.PageDto;
import com.yil.workflow.dto.CreateTaskStatusDto;
import com.yil.workflow.dto.TaskStatusDto;
import com.yil.workflow.model.TaskStatus;
import com.yil.workflow.service.TaskStatusService;
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
@RequestMapping(value = "v1/task-status")
public class TaskStatusController {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final TaskStatusService taskStatusService;

    @Autowired
    public TaskStatusController(TaskStatusService taskStatusService) {
        this.taskStatusService = taskStatusService;
    }

    @GetMapping
    public ResponseEntity<PageDto<TaskStatusDto>> findAll(
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) {
        try {
            if (page < 0)
                page = 0;
            if (size <= 0 || size > 1000)
                size = 1000;
            Pageable pageable = PageRequest.of(page, size);
            Page<TaskStatus> taskStatusPage = taskStatusService.findAllByDeletedTimeIsNull(pageable);
            PageDto<TaskStatusDto> pageDto = PageDto.toDto(taskStatusPage, TaskStatusService::toDto);
            return ResponseEntity.ok(pageDto);
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<TaskStatusDto> findById(@PathVariable Long id) {
        try {
            TaskStatus taskStatus;
            try {
                taskStatus = taskStatusService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            TaskStatusDto dto = TaskStatusService.toDto(taskStatus);
            return ResponseEntity.ok(dto);
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @Valid @RequestBody CreateTaskStatusDto dto) {
        try {
            TaskStatus taskStatus = new TaskStatus();
            taskStatus.setName(dto.getName());
            taskStatus.setCreatedUserId(authenticatedUserId);
            taskStatus.setCreatedTime(new Date());
            taskStatus = taskStatusService.save(taskStatus);
            return ResponseEntity.created(null).build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                  @PathVariable Long id,
                                  @Valid @RequestBody CreateTaskStatusDto dto) {
        try {
            TaskStatus taskStatus = null;
            try {
                taskStatus = taskStatusService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            taskStatus.setName(dto.getName());
            taskStatus = taskStatusService.save(taskStatus);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                         @PathVariable Long id) {
        try {
            TaskStatus taskStatus;
            try {
                taskStatus = taskStatusService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            taskStatus.setDeletedUserId(authenticatedUserId);
            taskStatus.setDeletedTime(new Date());
            taskStatusService.save(taskStatus);
            return ResponseEntity.ok("TaskStatus deleted.");
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


}
