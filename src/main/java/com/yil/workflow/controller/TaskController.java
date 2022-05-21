package com.yil.workflow.controller;

import com.yil.workflow.base.ApiHeaders;
import com.yil.workflow.base.PageDto;
import com.yil.workflow.dto.CreateTaskDto;
import com.yil.workflow.dto.TaskDto;
import com.yil.workflow.model.Task;
import com.yil.workflow.service.TaskService;
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
@RequestMapping(value = "v1/tasks")
public class TaskController {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<PageDto<TaskDto>> findAll(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "1000") int size) {
        try {
            if (page < 0)
                page = 0;
            if (size <= 0 || size > 1000)
                size = 1000;
            Pageable pageable = PageRequest.of(page, size);
            Page<Task> taskPage = taskService.findAllByDeletedTimeIsNull(pageable);
            PageDto<TaskDto> pageDto = PageDto.toDto(taskPage, TaskService::toDto);
            return ResponseEntity.ok(pageDto);
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<TaskDto> findById(@PathVariable Long id) {
        try {
            Task task;
            try {
                task = taskService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            TaskDto dto = TaskService.toDto(task);
            return ResponseEntity.ok(dto);
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @Valid @RequestBody CreateTaskDto dto) {
        try {
            Task task = new Task();
            task.setFlowId(dto.getFlowId());
            task.setTaskStatusId(dto.getTaskStatusId());
            task.setCurrentTaskActionId(dto.getCurrentTaskActionId());
            task.setStartDate(dto.getStartDate());
            task.setFinishDate(dto.getFinishDate());
            task.setEstimatedFinishDate(dto.getEstimatedFinishDate());
            task.setCreatedUserId(authenticatedUserId);
            task.setCreatedTime(new Date());
            task = taskService.save(task);
            return ResponseEntity.created(null).build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity replace(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                  @PathVariable Long id,
                                  @Valid @RequestBody CreateTaskDto dto) {
        try {
            Task task =null;
            try {
                task = taskService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            task.setFlowId(dto.getFlowId());
            task.setTaskStatusId(dto.getTaskStatusId());
            task.setCurrentTaskActionId(dto.getCurrentTaskActionId());
            task.setStartDate(dto.getStartDate());
            task.setFinishDate(dto.getFinishDate());
            task.setEstimatedFinishDate(dto.getEstimatedFinishDate());
            task = taskService.save(task);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                         @PathVariable Long id) {
        try {
            Task task;
            try {
                task = taskService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            task.setDeletedUserId(authenticatedUserId);
            task.setDeletedTime(new Date());
            taskService.save(task);
            return ResponseEntity.ok("Task deleted.");
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


}
