package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.PageDto;
import com.yil.workflow.dto.CreateStatusDto;
import com.yil.workflow.dto.StatusDto;
import com.yil.workflow.model.Status;
import com.yil.workflow.service.TaskStatusService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
@RequestMapping(value = "/api/wf/v1/status")
public class StatusController {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final TaskStatusService taskStatusService;

    @GetMapping
    public ResponseEntity<PageDto<StatusDto>> findAll(
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) {
        try {
            if (page < 0)
                page = 0;
            if (size <= 0 || size > 1000)
                size = 1000;
            Pageable pageable = PageRequest.of(page, size);
            Page<Status> taskStatusPage = taskStatusService.findAllByDeletedTimeIsNull(pageable);
            PageDto<StatusDto> pageDto = PageDto.toDto(taskStatusPage, TaskStatusService::toDto);
            return ResponseEntity.ok(pageDto);
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<StatusDto> findById(@PathVariable Long id) {
        try {
            Status status;
            try {
                status = taskStatusService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            StatusDto dto = TaskStatusService.toDto(status);
            return ResponseEntity.ok(dto);
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @Valid @RequestBody CreateStatusDto dto) {
        try {
            Status status = new Status();
            status.setName(dto.getName());
            status.setIsClosed(dto.getIsClosed());
            status.setCreatedUserId(authenticatedUserId);
            status.setCreatedTime(new Date());
            status = taskStatusService.save(status);
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
                                  @Valid @RequestBody CreateStatusDto dto) {
        try {
            Status status = null;
            try {
                status = taskStatusService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            status.setName(dto.getName());
            status.setIsClosed(dto.getIsClosed());
            status = taskStatusService.save(status);
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
            Status status;
            try {
                status = taskStatusService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            status.setDeletedUserId(authenticatedUserId);
            status.setDeletedTime(new Date());
            taskStatusService.save(status);
            return ResponseEntity.ok("TaskStatus deleted.");
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


}
