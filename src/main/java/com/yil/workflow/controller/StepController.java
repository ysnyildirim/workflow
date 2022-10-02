package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.Mapper;
import com.yil.workflow.dto.StepDto;
import com.yil.workflow.dto.StepRequest;
import com.yil.workflow.dto.StepResponse;
import com.yil.workflow.exception.FlowNotFoundException;
import com.yil.workflow.exception.StatusNotFoundException;
import com.yil.workflow.exception.StepNotFoundException;
import com.yil.workflow.exception.StepTypeNotFoundException;
import com.yil.workflow.model.Step;
import com.yil.workflow.service.StepService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/wf/v1/flows/{flowId}/steps")
public class StepController {

    private final StepService stepService;
    private final Mapper<Step, StepDto> mapper = new Mapper<>(StepService::convert);

    @GetMapping
    public ResponseEntity<StepDto[]> findAll(@PathVariable Long flowId) {
        StepDto[] stepDtos = mapper.map(stepService.findAllByFlowIdAndDeletedTimeIsNull(flowId)).toArray(StepDto[]::new);
        return ResponseEntity.ok(stepDtos);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<StepDto> findByIdAndFlowIdAndDeletedTimeIsNull(@PathVariable Long flowId,
                                                                         @PathVariable Long id) throws StepNotFoundException {
        StepDto dto = mapper.map(stepService.findByIdAndFlowIdAndDeletedTimeIsNull(id, flowId));
        return ResponseEntity.ok(dto);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<StepResponse> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                               @PathVariable Long flowId,
                                               @Valid @RequestBody StepRequest request) throws FlowNotFoundException, StatusNotFoundException, StepTypeNotFoundException {
        StepResponse responce = stepService.save(request, flowId, authenticatedUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responce);
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<StepResponse> replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                @PathVariable Long flowId,
                                                @PathVariable Long id,
                                                @Valid @RequestBody StepRequest request) throws StepNotFoundException, StatusNotFoundException, StepTypeNotFoundException {
        StepResponse responce = stepService.replace(request, id, authenticatedUserId);
        return ResponseEntity.ok(responce);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                         @PathVariable Long flowId,
                                         @PathVariable Long id) throws StepNotFoundException {
        stepService.delete(id, authenticatedUserId);
        return ResponseEntity.ok("Step deleted.");
    }


}
