package com.yil.workflow.controller;

import com.yil.workflow.dto.PriorityDto;
import com.yil.workflow.exception.PriorityNotFoundException;
import com.yil.workflow.model.Priority;
import com.yil.workflow.service.PriorityService;
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

    private final PriorityService priorityService;

    @GetMapping
    public ResponseEntity<List<PriorityDto>> findAll() {
        List<Priority> data = priorityService.findAll();
        List<PriorityDto> dto = new ArrayList<>();
        data.forEach(f -> {
            dto.add(PriorityService.toDto(f));
        });
        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PriorityDto> findById(@PathVariable Integer id) throws PriorityNotFoundException {
        Priority priority = priorityService.findById(id);
        PriorityDto dto = PriorityService.toDto(priority);
        return ResponseEntity.ok(dto);
    }

}
