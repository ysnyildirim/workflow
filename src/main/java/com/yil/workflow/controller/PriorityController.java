package com.yil.workflow.controller;

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

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/wf/v1/priorities")
public class PriorityController {

    private final PriorityTypeService priorityTypeService;

    @GetMapping
    public ResponseEntity<List<PriorityTypeDto>> findAll() {
        List<PriorityType> data = priorityTypeService.findAll();
        List<PriorityTypeDto> dto = new ArrayList<>();
        data.forEach(f -> {
            dto.add(PriorityTypeService.toDto(f));
        });
        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PriorityTypeDto> findById(@PathVariable Integer id) throws PriorityNotFoundException {
        PriorityType priority = priorityTypeService.findById(id);
        PriorityTypeDto dto = PriorityTypeService.toDto(priority);
        return ResponseEntity.ok(dto);
    }

}
