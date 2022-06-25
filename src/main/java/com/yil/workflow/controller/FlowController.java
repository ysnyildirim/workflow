package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.PageDto;
import com.yil.workflow.dto.FlowDto;
import com.yil.workflow.dto.FlowRequest;
import com.yil.workflow.dto.FlowResponce;
import com.yil.workflow.exception.FlowNotFoundException;
import com.yil.workflow.model.Flow;
import com.yil.workflow.service.FlowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/wf/v1/flows")
public class FlowController {

    private final FlowService flowService;

    @GetMapping
    public ResponseEntity<PageDto<FlowDto>> findAll(
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        Pageable pageable = PageRequest.of(page, size);
        Page<Flow> flowPage = flowService.findAllByDeletedTimeIsNull(pageable);
        PageDto<FlowDto> pageDto = PageDto.toDto(flowPage, FlowService::toDto);
        return ResponseEntity.ok(pageDto);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<FlowDto> findById(@PathVariable Long id) throws FlowNotFoundException {
        Flow flow = flowService.findByIdAndDeletedTimeIsNull(id);
        FlowDto dto = FlowService.toDto(flow);
        return ResponseEntity.ok(dto);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<FlowResponce> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                               @Valid @RequestBody FlowRequest request) {
        FlowResponce responce = flowService.save(request, authenticatedUserId);
        return ResponseEntity.created(null).body(responce);
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<FlowResponce> replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                @PathVariable Long id,
                                                @Valid @RequestBody FlowRequest request) throws FlowNotFoundException {
        FlowResponce responce = flowService.replace(request, id,authenticatedUserId);
        return ResponseEntity.ok(responce);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                         @PathVariable Long id) throws FlowNotFoundException {
        flowService.delete(id, authenticatedUserId);
        return ResponseEntity.ok("Flow deleted.");
    }


}
