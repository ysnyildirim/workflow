package com.yil.workflow.controller;

import com.yil.workflow.base.Mapper;
import com.yil.workflow.dto.StepTypeDto;
import com.yil.workflow.exception.StepTypeNotFoundException;
import com.yil.workflow.model.StepType;
import com.yil.workflow.service.StepTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/wf/v1/step-types")
public class StepTypeController {
    private final StepTypeService stepTypeService;
    private final Mapper<StepType, StepTypeDto> mapper = new Mapper<>(StepTypeService::convert);

    @GetMapping
    public ResponseEntity<StepTypeDto[]> findAll() {
        StepTypeDto[] dto = mapper.map(stepTypeService.findAll()).toArray(StepTypeDto[]::new);
        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<StepTypeDto> findById(@PathVariable Integer id) throws StepTypeNotFoundException {
        StepTypeDto dto = mapper.map(stepTypeService.findById(id));
        return ResponseEntity.ok(dto);
    }
}
