package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.Mapper;
import com.yil.workflow.dto.ActionDto;
import com.yil.workflow.dto.ActionRequest;
import com.yil.workflow.dto.ActionResponse;
import com.yil.workflow.exception.*;
import com.yil.workflow.model.Action;
import com.yil.workflow.service.ActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/wf/v1/steps/{stepId}/actions")
public class ActionController {

    private final ActionService actionService;
    private final Mapper<Action, ActionDto> mapper = new Mapper<>(ActionService::convert);

    @GetMapping
    public ResponseEntity<List<ActionDto>> findAll(@PathVariable Long stepId) {
        List<ActionDto> actions = mapper.map(actionService.findAll(stepId));
        return ResponseEntity.ok(actions);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<ActionDto> findByIdAndStepId(@PathVariable Long stepId,
                                                       @PathVariable Long id) throws ActionNotFoundException {
        ActionDto dto = mapper.map(actionService.findByIdAndStepId(id, stepId));
        return ResponseEntity.ok(dto);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ActionResponse> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                 @PathVariable Long stepId,
                                                 @Valid @RequestBody ActionRequest request) throws StepNotFoundException, TargetNotFoundException, GroupNotFoundException, UserNotFoundException {
        ActionResponse responce = actionService.save(request, stepId, authenticatedUserId);
        return ResponseEntity.created(null).body(responce);
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ActionResponse> replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                  @PathVariable Long stepId,
                                                  @PathVariable Long id,
                                                  @Valid @RequestBody ActionRequest request) throws ActionNotFoundException, StepNotFoundException, TargetNotFoundException, GroupNotFoundException, UserNotFoundException {
        ActionResponse responce = actionService.replace(request, id, authenticatedUserId);
        return ResponseEntity.ok(responce);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                         @PathVariable Long stepId,
                                         @PathVariable Long id) throws ActionNotFoundException {
        actionService.delete(id, authenticatedUserId);
        return ResponseEntity.ok("Action deleted.");
    }

}
