package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.Mapper;
import com.yil.workflow.dto.FlowDto;
import com.yil.workflow.dto.FlowRequest;
import com.yil.workflow.dto.FlowResponse;
import com.yil.workflow.exception.FlowNotFoundException;
import com.yil.workflow.model.Flow;
import com.yil.workflow.service.FlowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/wf/v1/flows")
public class FlowController {
    private final FlowService flowService;
    private final Mapper<Flow, FlowDto> mapper = new Mapper<>(FlowService::convert);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<FlowDto[]> findAll() {
        FlowDto[] dto = mapper.map(flowService.findAll()).toArray(FlowDto[]::new);
        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<FlowDto> findById(@PathVariable Long id) throws FlowNotFoundException {
        FlowDto dto = mapper.map(flowService.findById(id));
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<FlowResponse> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                               @Valid @RequestBody FlowRequest request) {
        FlowResponse responce = flowService.save(request, authenticatedUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responce);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<FlowResponse> replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                @PathVariable Long id,
                                                @Valid @RequestBody FlowRequest request) throws FlowNotFoundException {
        FlowResponse responce = flowService.replace(request, id, authenticatedUserId);
        return ResponseEntity.ok(responce);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                         @PathVariable Long id) throws FlowNotFoundException {
        flowService.delete(id, authenticatedUserId);
        return ResponseEntity.ok("Flow deleted.");
    }

    @GetMapping(value = "/start")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<FlowDto[]> getStartUpFlows() {
        FlowDto[] dto = mapper.map(flowService.findAllByEnabledTrue()).toArray(FlowDto[]::new);
        return ResponseEntity.ok(dto);
    }
}
