package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.Mapper;
import com.yil.workflow.base.PageDto;
import com.yil.workflow.dto.StatusDto;
import com.yil.workflow.exception.StatusNotFoundException;
import com.yil.workflow.model.Status;
import com.yil.workflow.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/wf/v1/status")
public class StatusController {

    private final StatusService statusService;
    private final Mapper<Status, StatusDto> mapper = new Mapper<>(StatusService::convert);

    @GetMapping
    public ResponseEntity<PageDto<StatusDto>> findAll(
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        Pageable pageable = PageRequest.of(page, size);
        PageDto<StatusDto> pageDto = mapper.map(statusService.findAll(pageable));
        return ResponseEntity.ok(pageDto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<StatusDto> findById(@PathVariable Integer id) throws StatusNotFoundException {
        StatusDto dto = mapper.map(statusService.findById(id));
        return ResponseEntity.ok(dto);
    }

}
