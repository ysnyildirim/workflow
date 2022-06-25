package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.PageDto;
import com.yil.workflow.dto.StatusDto;
import com.yil.workflow.exception.StatusNotFoundException;
import com.yil.workflow.model.Status;
import com.yil.workflow.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/wf/v1/status")
public class StatusController {

    private final StatusService statusService;

    @GetMapping
    public ResponseEntity<PageDto<StatusDto>> findAll(
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        Pageable pageable = PageRequest.of(page, size);
        Page<Status> taskStatusPage = statusService.findAll(pageable);
        PageDto<StatusDto> pageDto = PageDto.toDto(taskStatusPage, StatusService::toDto);
        return ResponseEntity.ok(pageDto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<StatusDto> findById(@PathVariable Integer id) throws StatusNotFoundException {
        Status status = statusService.findById(id);
        StatusDto dto = StatusService.toDto(status);
        return ResponseEntity.ok(dto);
    }

}
