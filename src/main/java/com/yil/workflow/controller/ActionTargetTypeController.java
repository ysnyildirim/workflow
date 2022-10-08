/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */
package com.yil.workflow.controller;

import com.yil.workflow.base.Mapper;
import com.yil.workflow.dto.ActionTargetTypeDto;
import com.yil.workflow.exception.ActionTargetTypeNotFoundException;
import com.yil.workflow.model.ActionTargetType;
import com.yil.workflow.service.ActionTargetTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/wf/v1/action-target-types")
public class ActionTargetTypeController {
    private final ActionTargetTypeService actionTargetTypeService;
    private final Mapper<ActionTargetType, ActionTargetTypeDto> mapper = new Mapper<>(ActionTargetTypeService::toDto);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ActionTargetTypeDto>> findAll() {
        return ResponseEntity.ok(mapper.map(actionTargetTypeService.findAll()));
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ActionTargetTypeDto> findById(@PathVariable Integer id) throws ActionTargetTypeNotFoundException {
        return ResponseEntity.ok(mapper.map(actionTargetTypeService.findById(id)));
    }
}
