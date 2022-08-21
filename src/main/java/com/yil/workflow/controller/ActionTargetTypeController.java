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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/wf/v1/action-target-types")
public class ActionTargetTypeController {

    private final ActionPermissionTypeService actionPermissionTypeService;
    private final Mapper<ActionPermissionType, ActionPermissionTypeDto> mapper = new Mapper<>(ActionPermissionTypeService::convert);

    @GetMapping
    public ResponseEntity<ActionPermissionTypeDto[]> findAll() {
        ActionPermissionTypeDto[] dto = mapper.map(actionPermissionTypeService.findAll()).toArray(ActionPermissionTypeDto[]::new);
        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ActionPermissionTypeDto> findById(@PathVariable Integer id) throws ActionPermissionTypeNotFoundException {
        ActionPermissionTypeDto dto = mapper.map(actionPermissionTypeService.findById(id));
        return ResponseEntity.ok(dto);
    }
}
