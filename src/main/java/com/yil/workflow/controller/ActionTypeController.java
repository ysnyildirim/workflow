package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.PageDto;
import com.yil.workflow.dto.ActionTypeDto;
import com.yil.workflow.exception.ActionTypeNotFound;
import com.yil.workflow.model.ActionType;
import com.yil.workflow.service.ActionTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/wf/v1/action-types")
public class ActionTypeController {

    private final ActionTypeService actionTypeService;

    @GetMapping
    public ResponseEntity<PageDto<ActionTypeDto>> findAll(
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        Pageable pageable = PageRequest.of(page, size);
        Page<ActionType> data = actionTypeService.findAll(pageable);
        PageDto<ActionTypeDto> pageDto = PageDto.toDto(data, ActionTypeService::toDto);
        return ResponseEntity.ok(pageDto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ActionTypeDto> findById(@PathVariable Integer id) throws ActionTypeNotFound {
        ActionType actionType = actionTypeService.findById(id);
        ActionTypeDto dto = ActionTypeService.toDto(actionType);
        return ResponseEntity.ok(dto);
    }

}
