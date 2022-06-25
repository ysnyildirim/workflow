package com.yil.workflow.controller;

import com.yil.workflow.base.PageDto;
import com.yil.workflow.dto.PriorityDto;
import com.yil.workflow.dto.StepTypeDto;
import com.yil.workflow.exception.StepTypeNotFoundException;
import com.yil.workflow.model.Priority;
import com.yil.workflow.model.StepType;
import com.yil.workflow.service.PriorityService;
import com.yil.workflow.service.StepTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/wf/v1/step-types")
public class StepTypeController {

    private final StepTypeService stepTypeService;

    @GetMapping
    public ResponseEntity<List<StepTypeDto>> findAll() {
        List<StepType> data = stepTypeService.findAll();
        List<StepTypeDto> dto = new ArrayList<>();
        data.forEach(f -> {
            dto.add(StepTypeService.toDto(f));
        });
        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<StepTypeDto> findById(@PathVariable Integer id) throws StepTypeNotFoundException {
        StepType stepType = stepTypeService.findById(id);
        StepTypeDto dto = StepTypeService.toDto(stepType);
        return ResponseEntity.ok(dto);
    }

}
