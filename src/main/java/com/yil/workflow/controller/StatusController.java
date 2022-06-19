package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.PageDto;
import com.yil.workflow.dto.CreateStatusDto;
import com.yil.workflow.dto.StatusDto;
import com.yil.workflow.exception.StatusNotFoundException;
import com.yil.workflow.model.Status;
import com.yil.workflow.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

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
        Page<Status> taskStatusPage = statusService.findAllByDeletedTimeIsNull(pageable);
        PageDto<StatusDto> pageDto = PageDto.toDto(taskStatusPage, StatusService::toDto);
        return ResponseEntity.ok(pageDto);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<StatusDto> findById(@PathVariable Long id) throws StatusNotFoundException {
        Status status = statusService.findByIdAndDeletedTimeIsNull(id);
        StatusDto dto = StatusService.toDto(status);
        return ResponseEntity.ok(dto);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<StatusDto> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                            @Valid @RequestBody CreateStatusDto request) {
        Status status = new Status();
        status.setName(request.getName());
        status.setIsClosed(request.getIsClosed());
        status.setCreatedUserId(authenticatedUserId);
        status.setCreatedTime(new Date());
        status = statusService.save(status);
        StatusDto dto = StatusService.toDto(status);
        return ResponseEntity.created(null).body(dto);
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                  @PathVariable Long id,
                                  @Valid @RequestBody CreateStatusDto request) throws StatusNotFoundException {
        Status status = statusService.findByIdAndDeletedTimeIsNull(id);
        status.setName(request.getName());
        status.setIsClosed(request.getIsClosed());
        status = statusService.save(status);
        StatusDto dto = StatusService.toDto(status);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                         @PathVariable Long id) throws StatusNotFoundException {
        Status status = statusService.findByIdAndDeletedTimeIsNull(id);
        status.setDeletedUserId(authenticatedUserId);
        status.setDeletedTime(new Date());
        statusService.save(status);
        return ResponseEntity.ok("TaskStatus deleted.");
    }


}
