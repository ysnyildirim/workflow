/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */
package com.yil.workflow.controller;

import com.yil.workflow.base.Mapper;
import com.yil.workflow.dto.ActionPermissionTypeDto;
import com.yil.workflow.exception.ActionPermissionTypeNotFoundException;
import com.yil.workflow.model.ActionPermissionType;
import com.yil.workflow.service.ActionPermissionTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/wf/v1/action-permission-types")
public class ActionPermissionTypeController {
    private final ActionPermissionTypeService actionPermissionTypeService;
    private final Mapper<ActionPermissionType, ActionPermissionTypeDto> mapper = new Mapper<>(ActionPermissionTypeService::convert);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ActionPermissionTypeDto>> findAll() {
        return ResponseEntity.ok(mapper.map(actionPermissionTypeService.findAll()));
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ActionPermissionTypeDto> findById(@PathVariable Integer id) throws ActionPermissionTypeNotFoundException {
        return ResponseEntity.ok(mapper.map(actionPermissionTypeService.findById(id)));
    }
}
