package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.Mapper;
import com.yil.workflow.base.PageDto;
import com.yil.workflow.dto.FlowRequest;
import com.yil.workflow.dto.StatusDto;
import com.yil.workflow.dto.StatusRequest;
import com.yil.workflow.dto.StatusResponse;
import com.yil.workflow.exception.StatusNotFoundException;
import com.yil.workflow.model.Status;
import com.yil.workflow.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/wf/v1/status")
public class StatusController {
    private final StatusService statusService;
    private final Mapper<Status, StatusDto> mapper = new Mapper<>(StatusService::convert);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
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
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<StatusDto> findById(@PathVariable Integer id) throws StatusNotFoundException {
        StatusDto dto = mapper.map(statusService.findById(id));
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<StatusResponse> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                 @Valid @RequestBody StatusRequest request) {
        Status status = statusService.save(request, authenticatedUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(StatusResponse.builder().id(status.getId()).build());
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<StatusResponse> replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                  @PathVariable Integer id,
                                                  @Valid @RequestBody FlowRequest request) throws StatusNotFoundException {
        Status status = statusService.replace(request, id, authenticatedUserId);
        return ResponseEntity.ok(StatusResponse.builder().id(status.getId()).build());
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                         @PathVariable Integer id) {
        statusService.delete(id, authenticatedUserId);
        return ResponseEntity.ok("Status deleted.");
    }
}
