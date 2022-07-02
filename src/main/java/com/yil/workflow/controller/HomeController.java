/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.dto.ActionDto;
import com.yil.workflow.dto.FlowDto;
import com.yil.workflow.exception.ActionNotFoundException;
import com.yil.workflow.exception.FlowNotFoundException;
import com.yil.workflow.exception.StepNotFoundException;
import com.yil.workflow.exception.TaskNotFoundException;
import com.yil.workflow.model.Flow;
import com.yil.workflow.service.ActionService;
import com.yil.workflow.service.FlowService;
import com.yil.workflow.service.TaskActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/wf/v1/home")
public class HomeController {

    private final TaskActionService taskActionService;
    private final FlowService flowService;
    private final ActionService actionService;

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "/{taskId}/actions")
    public ResponseEntity<List<ActionDto>> getNextActions(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                          @PathVariable Long taskId) throws ActionNotFoundException, StepNotFoundException, TaskNotFoundException {
        List<ActionDto> actions = taskActionService.getNextActions(taskId, authenticatedUserId);
        return ResponseEntity.ok(actions);
    }

    @GetMapping(value = "/flows")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<FlowDto>> getStartUpFlows(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId) {
        List<Flow> data = flowService.getStartUpFlows(authenticatedUserId);
        List<FlowDto> dto = new ArrayList<>();
        data.forEach(f -> {
            dto.add(FlowService.toDto(f));
        });
        return ResponseEntity.ok(dto);
    }


    @GetMapping(value = "/flows/{flowId}/actions")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ActionDto>> getStartActions(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                           @PathVariable Long flowId) throws FlowNotFoundException {
        if (!flowService.existsByIdAndEnabledTrueAndDeletedTimeIsNull(flowId))
            throw new FlowNotFoundException();
        List<ActionDto> actionDtos = actionService.getStartUpActions(flowId, authenticatedUserId);
        return ResponseEntity.ok(actionDtos);
    }
}