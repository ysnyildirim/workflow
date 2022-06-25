package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.PageDto;
import com.yil.workflow.dto.PriorityDto;
import com.yil.workflow.exception.PriorityNotFoundException;
import com.yil.workflow.model.Priority;
import com.yil.workflow.service.PriorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/wf/v1/priorities")
public class PriorityController {

    private final PriorityService priorityService;

    @GetMapping
    public ResponseEntity<PageDto<PriorityDto>> findAll(
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        Pageable pageable = PageRequest.of(page, size);
        Page<Priority> data = priorityService.findAll(pageable);
        PageDto<PriorityDto> pageDto = PageDto.toDto(data, PriorityService::toDto);
        return ResponseEntity.ok(pageDto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PriorityDto> findById(@PathVariable Integer id) throws PriorityNotFoundException {
        Priority priority = priorityService.findById(id);
        PriorityDto dto = PriorityService.toDto(priority);
        return ResponseEntity.ok(dto);
    }

}
