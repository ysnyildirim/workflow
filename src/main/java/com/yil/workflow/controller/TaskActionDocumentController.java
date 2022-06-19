package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.PageDto;
import com.yil.workflow.dto.CreateTaskActionDocumentDto;
import com.yil.workflow.dto.TaskActionDocumentDto;
import com.yil.workflow.model.TaskActionDocument;
import com.yil.workflow.service.TaskActionDocumentService;
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
@RequestMapping(value = "/api/wf/v1/task-actions/{taskActionId}/documents")
public class TaskActionDocumentController {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final TaskActionDocumentService taskActionDocumentService;

    @GetMapping
    public ResponseEntity<PageDto<TaskActionDocumentDto>> findAll(
            @PathVariable Long taskActionId,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) {
        try {
            if (page < 0)
                page = 0;
            if (size <= 0 || size > 1000)
                size = 1000;
            Pageable pageable = PageRequest.of(page, size);
            Page<TaskActionDocument> taskPage = taskActionDocumentService.findAllByTaskActionIdAndDeletedTimeIsNull(pageable, taskActionId);
            PageDto<TaskActionDocumentDto> pageDto = PageDto.toDto(taskPage, TaskActionDocumentService::toDto);
            return ResponseEntity.ok(pageDto);
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<TaskActionDocumentDto> findById(
            @PathVariable Long taskActionId,
            @PathVariable Long id) {
        try {
            TaskActionDocument task;
            try {
                task = taskActionDocumentService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            if (!task.getTaskActionId().equals(taskActionId))
                return ResponseEntity.notFound().build();
            TaskActionDocumentDto dto = TaskActionDocumentService.toDto(task);
            return ResponseEntity.ok(dto);
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long taskActionId,
                                 @Valid @RequestBody CreateTaskActionDocumentDto dto) {
        try {
            TaskActionDocument entity = new TaskActionDocument();
            entity.setTaskActionId(taskActionId);
            entity.setDocumentId(dto.getDocumentId());
            entity.setCreatedUserId(authenticatedUserId);
            entity.setCreatedTime(new Date());
            entity = taskActionDocumentService.save(entity);
            return ResponseEntity.created(null).build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                  @PathVariable Long taskActionId,
                                  @PathVariable Long id,
                                  @Valid @RequestBody CreateTaskActionDocumentDto dto) {
        try {
            TaskActionDocument entity = null;
            try {
                entity = taskActionDocumentService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            if (!entity.getTaskActionId().equals(taskActionId))
                return ResponseEntity.notFound().build();
            entity.setDocumentId(dto.getDocumentId());
            entity = taskActionDocumentService.save(entity);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                         @PathVariable Long taskActionId,
                                         @PathVariable Long id) {
        try {
            TaskActionDocument entity;
            try {
                entity = taskActionDocumentService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            if (!entity.getTaskActionId().equals(taskActionId))
                return ResponseEntity.notFound().build();
            entity.setDeletedUserId(authenticatedUserId);
            entity.setDeletedTime(new Date());
            entity = taskActionDocumentService.save(entity);
            return ResponseEntity.ok("Action email deleted.");
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


}
