package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.PageDto;
import com.yil.workflow.dto.CreateStepDto;
import com.yil.workflow.dto.StepDto;
import com.yil.workflow.exception.StepNotFoundException;
import com.yil.workflow.model.Step;
import com.yil.workflow.service.StepService;
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
@RequestMapping(value = "/api/wf/v1/flows/{flowId}/steps")
public class StepController {

    private final StepService stepService;

    @GetMapping
    public ResponseEntity<PageDto<StepDto>> findAll(
            @PathVariable Long flowId,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        Pageable pageable = PageRequest.of(page, size);
        Page<Step> stepPage = stepService.findAllByFlowIdAndDeletedTimeIsNull(pageable, flowId);
        PageDto<StepDto> pageDto = PageDto.toDto(stepPage, StepService::toDto);
        return ResponseEntity.ok(pageDto);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<StepDto> findByIdAndFlowIdAndDeletedTimeIsNull(@PathVariable Long flowId,
                                                                         @PathVariable Long id) throws StepNotFoundException {
        Step step = stepService.findByIdAndFlowIdAndDeletedTimeIsNull(id, flowId);
        StepDto dto = StepService.toDto(step);
        return ResponseEntity.ok(dto);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<StepDto> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                          @PathVariable Long flowId,
                                          @Valid @RequestBody CreateStepDto request) {
        Step step = new Step();
        step.setName(request.getName());
        step.setDescription(request.getDescription());
        step.setEnabled(request.getEnabled());
        step.setFlowId(flowId);
        step.setCreatedUserId(authenticatedUserId);
        step.setCreatedTime(new Date());
        step = stepService.save(step);
        StepDto dto = StepService.toDto(step);
        return ResponseEntity.created(null).body(dto);
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<StepDto> replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                           @PathVariable Long flowId,
                                           @PathVariable Long id,
                                           @Valid @RequestBody CreateStepDto request) throws StepNotFoundException {
        Step step = stepService.findByIdAndFlowIdAndDeletedTimeIsNull(id, flowId);
        step.setName(request.getName());
        step.setDescription(request.getDescription());
        step.setEnabled(request.getEnabled());
        step.setFlowId(flowId);
        step = stepService.save(step);
        StepDto dto = StepService.toDto(step);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                         @PathVariable Long flowId,
                                         @PathVariable Long id) throws StepNotFoundException {
        Step step = stepService.findByIdAndFlowIdAndDeletedTimeIsNull(id, flowId);
        step.setDeletedUserId(authenticatedUserId);
        step.setDeletedTime(new Date());
        stepService.save(step);
        return ResponseEntity.ok("Step deleted.");
    }


}
