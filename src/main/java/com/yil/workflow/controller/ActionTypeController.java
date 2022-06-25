package com.yil.workflow.controller;

import com.yil.workflow.base.PageDto;
import com.yil.workflow.dto.ActionTypeDto;
import com.yil.workflow.exception.ActionTypeNotFoundException;
import com.yil.workflow.model.ActionType;
import com.yil.workflow.service.ActionTypeService;
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
@RequestMapping(value = "/api/wf/v1/action-types")
public class ActionTypeController {

    private final ActionTypeService actionTypeService;

    @GetMapping
    public ResponseEntity<List<ActionTypeDto>> findAll() {
        List<ActionType> data = actionTypeService.findAll();
        List<ActionTypeDto> dto = new ArrayList<>();
        data.forEach(f -> {
            dto.add(ActionTypeService.toDto(f));
        });
        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ActionTypeDto> findById(@PathVariable Integer id) throws ActionTypeNotFoundException {
        ActionType actionType = actionTypeService.findById(id);
        ActionTypeDto dto = ActionTypeService.toDto(actionType);
        return ResponseEntity.ok(dto);
    }

}
