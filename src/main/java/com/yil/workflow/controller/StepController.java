package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
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
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/wf/v1/flows/{flowId}/steps")
public class StepController {

    private final StepService stepService;

    @GetMapping
    public ResponseEntity<List<StepDto>> findAll(@PathVariable Long flowId) {
        List<Step> stepList = stepService.findAllByFlowIdAndDeletedTimeIsNull(flowId);
        List<StepDto> stepDtos = new ArrayList<>();
        stepList.forEach(f -> {
            stepDtos.add(StepService.toDto(f));
        });
        return ResponseEntity.ok(stepDtos);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<StepDto> findByIdAndFlowIdAndDeletedTimeIsNull(@PathVariable Long flowId,
                                                                         @PathVariable Long id) throws StepNotFoundException {
        Step step = stepService.findByIdAndFlowIdAndDeletedTimeIsNull(id, flowId);
        StepDto dto = StepService.toDto(step);
        return ResponseEntity.ok(dto);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<StepResponse> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                               @PathVariable Long flowId,
                                               @Valid @RequestBody StepRequest request) throws FlowNotFoundException, StatusNotFoundException, StepTypeNotFoundException {
        StepResponse responce = stepService.save(request, flowId, authenticatedUserId);
        return ResponseEntity.created(null).body(responce);
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
