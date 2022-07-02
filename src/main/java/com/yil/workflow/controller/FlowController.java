package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
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
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/wf/v1/flows")
public class FlowController {

    private final FlowService flowService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<FlowDto>> findAll() {
        List<Flow> data = flowService.findAllByDeletedTimeIsNull();
        List<FlowDto> dto = new ArrayList<>();
        data.forEach(f -> {
            dto.add(FlowService.toDto(f));
        });
        return ResponseEntity.ok(dto);
    }


    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<FlowDto> findById(@PathVariable Long id) throws FlowNotFoundException {
        Flow flow = flowService.findByIdAndDeletedTimeIsNull(id);
        FlowDto dto = FlowService.toDto(flow);
        return ResponseEntity.ok(dto);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<FlowResponse> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                               @Valid @RequestBody FlowRequest request) {
        FlowResponse responce = flowService.save(request, authenticatedUserId);
        return ResponseEntity.created(null).body(responce);
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


}
