package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.PageDto;
import com.yil.workflow.dto.StepTypeDto;
import com.yil.workflow.exception.StepTypeNotFoundException;
import com.yil.workflow.model.StepType;
import com.yil.workflow.service.StepTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/wf/v1/step-types")
public class StepTypeController {

    private final StepTypeService stepTypeService;

    @GetMapping
    public ResponseEntity<PageDto<StepTypeDto>> findAll(
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        Pageable pageable = PageRequest.of(page, size);
        Page<StepType> data = stepTypeService.findAll(pageable);
        PageDto<StepTypeDto> pageDto = PageDto.toDto(data, StepTypeService::toDto);
        return ResponseEntity.ok(pageDto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<StepTypeDto> findById(@PathVariable Integer id) throws StepTypeNotFoundException {
        StepType stepType = stepTypeService.findById(id);
        StepTypeDto dto = StepTypeService.toDto(stepType);
        return ResponseEntity.ok(dto);
    }

}
