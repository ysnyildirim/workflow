/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */
package com.yil.workflow.controller;

import com.yil.workflow.base.Mapper;
import com.yil.workflow.dto.ActionNotificationTargetTypeDto;
import com.yil.workflow.exception.ActionNotificationTargetTypeNotFoundException;
import com.yil.workflow.model.ActionNotificationTargetType;
import com.yil.workflow.service.ActionNotificationTargetTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/wf/v1/action-notification-target-types")
public class ActionNotificationTargetTypeController {
    private final ActionNotificationTargetTypeService actionNotificationTargetTypeService;
    private final Mapper<ActionNotificationTargetType, ActionNotificationTargetTypeDto> mapper = new Mapper<>(ActionNotificationTargetTypeService::toDto);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ActionNotificationTargetTypeDto>> findAll() {
        return ResponseEntity.ok(mapper.map(actionNotificationTargetTypeService.findAll()));
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ActionNotificationTargetTypeDto> findById(@PathVariable Integer id) throws ActionNotificationTargetTypeNotFoundException {
        return ResponseEntity.ok(mapper.map(actionNotificationTargetTypeService.findById(id)));
    }
}
