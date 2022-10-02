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

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/wf/v1/")
public class ActionController {

    private final ActionService actionService;
    private final Mapper<Action, ActionDto> mapper = new Mapper<>(ActionService::convert);

    @GetMapping(value = "/steps/{stepId}/actions")
    public ResponseEntity<ActionDto[]> findAll(@PathVariable Long stepId) {
        ActionDto[] actions = mapper.map(actionService.findAll(stepId)).toArray(ActionDto[]::new);
        return ResponseEntity.ok(actions);
    }


    @GetMapping(value = "/steps/{stepId}/actions/{id}")
    public ResponseEntity<ActionDto> findByIdAndStepId(@PathVariable Long stepId,
                                                       @PathVariable Long id) throws ActionNotFoundException {
        ActionDto dto = mapper.map(actionService.findByIdAndStepId(id, stepId));
        return ResponseEntity.ok(dto);
    }


    @PostMapping(value = "/steps/{stepId}/actions")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ActionResponse> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                 @PathVariable Long stepId,
                                                 @Valid @RequestBody ActionRequest request) throws StepNotFoundException, CannotBeAddedToThisStepException, ActionPermissionTypeNotFoundException, ActionTargetTypeNotFoundException {
        ActionResponse responce = actionService.save(request, stepId, authenticatedUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responce);
    }


    @PutMapping("/steps/{stepId}/actions/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ActionResponse> replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                  @PathVariable Long stepId,
                                                  @PathVariable Long id,
                                                  @Valid @RequestBody ActionRequest request) throws ActionNotFoundException, StepNotFoundException, ActionPermissionTypeNotFoundException, ActionTargetTypeNotFoundException {
        ActionResponse responce = actionService.replace(request, id, authenticatedUserId);
        return ResponseEntity.ok(responce);
    }

    @DeleteMapping(value = "/steps/{stepId}/actions/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                         @PathVariable Long stepId,
                                         @PathVariable Long id) throws ActionNotFoundException {
        actionService.delete(id, authenticatedUserId);
        return ResponseEntity.ok("Action deleted.");
    }


    @GetMapping(value = "/actions/starts/{flowId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ActionDto[]> getStartUpActions(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                         @PathVariable Long flowId) {
        ActionDto[] dto = mapper.map(actionService.getStartActions(flowId, authenticatedUserId)).toArray(ActionDto[]::new);
        return ResponseEntity.ok(dto);
    }

}
