package com.yil.workflow.controller;

import com.yil.workflow.base.ApiHeaders;
import com.yil.workflow.base.PageDto;
import com.yil.workflow.dto.CreateStepDto;
import com.yil.workflow.dto.StepDto;
import com.yil.workflow.model.Step;
import com.yil.workflow.model.Task;
import com.yil.workflow.service.StepService;
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
@RequestMapping(value = "v1/steps")
public class StepController {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final StepService stepService;

    @Autowired
    public StepController(StepService stepService) {
        this.stepService = stepService;
    }

    @GetMapping
    public ResponseEntity<PageDto<StepDto>> findAll(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "1000") int size) {
        try {
            if (page < 0)
                page = 0;
            if (size <= 0 || size > 1000)
                size = 1000;
            Pageable pageable = PageRequest.of(page, size);
            Page<Step> stepPage = stepService.findAllByDeletedTimeIsNull(pageable);
            PageDto<StepDto> pageDto = PageDto.toDto(stepPage, StepService::toDto);
            return ResponseEntity.ok(pageDto);
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<StepDto> findById(@PathVariable Long id) {
        try {
            Step step;
            try {
                step = stepService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            StepDto dto = StepService.toDto(step);
            return ResponseEntity.ok(dto);
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @Valid @RequestBody CreateStepDto dto) {
        try {
            Step step = new Step();
            step.setName(dto.getName());
            step.setDescription(dto.getDescription());
            step.setEnabled(dto.getEnabled());
            step.setFlowId(dto.getFlowId());
            step.setCreatedUserId(authenticatedUserId);
            step.setCreatedTime(new Date());
            step = stepService.save(step);
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
                                  @Valid @RequestBody CreateStepDto dto) {
        try {
            Step step =null;
            try {
                step = stepService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            step.setName(dto.getName());
            step.setDescription(dto.getDescription());
            step.setEnabled(dto.getEnabled());
            step.setFlowId(dto.getFlowId());
            step = stepService.save(step);
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
            Step step;
            try {
                step = stepService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            step.setDeletedUserId(authenticatedUserId);
            step.setDeletedTime(new Date());
            stepService.save(step);
            return ResponseEntity.ok("Step deleted.");
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


}
