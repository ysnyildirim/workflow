package com.yil.workflow.controller;

import com.yil.workflow.base.Mapper;
import com.yil.workflow.dto.PriorityTypeDto;
import com.yil.workflow.exception.PriorityNotFoundException;
import com.yil.workflow.model.PriorityType;
import com.yil.workflow.service.PriorityTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/wf/v1/priorities")
public class PriorityController {

    private final PriorityTypeService priorityTypeService;
    private final Mapper<PriorityType, PriorityTypeDto> mapper = new Mapper<>(PriorityTypeService::convert);

    @GetMapping
    public ResponseEntity<PriorityTypeDto[]> findAll() {
        PriorityTypeDto[] dto = mapper.map(priorityTypeService.findAll()).toArray(PriorityTypeDto[]::new);
        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PriorityTypeDto> findById(@PathVariable Integer id) throws PriorityNotFoundException {
        PriorityTypeDto dto = mapper.map(priorityTypeService.findById(id));
        return ResponseEntity.ok(dto);
    }

}
