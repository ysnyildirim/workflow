package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.PageDto;
import com.yil.workflow.dto.CreateFlowDto;
import com.yil.workflow.dto.FlowDto;
import com.yil.workflow.model.Flow;
import com.yil.workflow.service.FlowService;
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
@RequestMapping(value = "v1/flows")
public class FlowController {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final FlowService flowService;

    @Autowired
    public FlowController(FlowService flowService) {
        this.flowService = flowService;
    }

    @GetMapping
    public ResponseEntity<PageDto<FlowDto>> findAll(
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) {
        try {
            if (page < 0)
                page = 0;
            if (size <= 0 || size > 1000)
                size = 1000;
            Pageable pageable = PageRequest.of(page, size);
            Page<Flow> flowPage = flowService.findAllByDeletedTimeIsNull(pageable);
            PageDto<FlowDto> pageDto = PageDto.toDto(flowPage, FlowService::toDto);
            return ResponseEntity.ok(pageDto);
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<FlowDto> findById(@PathVariable Long id) {
        try {
            Flow flow;
            try {
                flow = flowService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            FlowDto dto = FlowService.toDto(flow);
            return ResponseEntity.ok(dto);
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @Valid @RequestBody CreateFlowDto dto) {
        try {
            Flow flow = new Flow();
            flow.setName(dto.getName());
            flow.setDescription(dto.getDescription());
            flow.setEnabled(dto.getEnabled());
            flow.setStartUpStepId(dto.getStartUpStepId());
            flow.setStartUpPermissionId(dto.getStartUpPermissionId());
            flow.setCreatedUserId(authenticatedUserId);
            flow.setCreatedTime(new Date());
            flow = flowService.save(flow);
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
                                  @Valid @RequestBody CreateFlowDto dto) {
        try {
            Flow flow =null;
            try {
                flow = flowService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            flow.setName(dto.getName());
            flow.setDescription(dto.getDescription());
            flow.setEnabled(dto.getEnabled());
            flow.setStartUpStepId(dto.getStartUpStepId());
            flow.setStartUpPermissionId(dto.getStartUpPermissionId());
            flow = flowService.save(flow);
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
            Flow flow;
            try {
                flow = flowService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            flow.setDeletedUserId(authenticatedUserId);
            flow.setDeletedTime(new Date());
            flowService.save(flow);
            return ResponseEntity.ok("Flow deleted.");
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


}
