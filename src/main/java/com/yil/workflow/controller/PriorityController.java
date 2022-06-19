package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.PageDto;
import com.yil.workflow.dto.CreatePriorityDto;
import com.yil.workflow.dto.PriorityDto;
import com.yil.workflow.model.Priority;
import com.yil.workflow.service.TaskPriorityService;
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
@RequestMapping(value = "/api/wf/v1/priorities")
public class PriorityController {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final TaskPriorityService taskPriorityService;

    @GetMapping
    public ResponseEntity<PageDto<PriorityDto>> findAll(
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) {
        try {
            if (page < 0)
                page = 0;
            if (size <= 0 || size > 1000)
                size = 1000;
            Pageable pageable = PageRequest.of(page, size);
            Page<Priority> taskPriorityPage = taskPriorityService.findAllByDeletedTimeIsNull(pageable);
            PageDto<PriorityDto> pageDto = PageDto.toDto(taskPriorityPage, TaskPriorityService::toDto);
            return ResponseEntity.ok(pageDto);
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<PriorityDto> findById(@PathVariable Long id) {
        try {
            Priority priority;
            try {
                priority = taskPriorityService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            PriorityDto dto = TaskPriorityService.toDto(priority);
            return ResponseEntity.ok(dto);
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @Valid @RequestBody CreatePriorityDto dto) {
        try {
            Priority priority = new Priority();
            priority.setName(dto.getName());
            priority.setDescription(dto.getDescription());
            priority.setCreatedUserId(authenticatedUserId);
            priority.setCreatedTime(new Date());
            priority = taskPriorityService.save(priority);
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
                                  @Valid @RequestBody CreatePriorityDto dto) {
        try {
            Priority priority = null;
            try {
                priority = taskPriorityService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            priority.setName(dto.getName());
            priority.setDescription(dto.getDescription());
            priority = taskPriorityService.save(priority);
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
            Priority priority;
            try {
                priority = taskPriorityService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            priority.setDeletedUserId(authenticatedUserId);
            priority.setDeletedTime(new Date());
            taskPriorityService.save(priority);
            return ResponseEntity.ok("TaskPriority deleted.");
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


}
