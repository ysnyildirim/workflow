/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.Mapper;
import com.yil.workflow.dto.ActionDto;
import com.yil.workflow.dto.FlowDto;
import com.yil.workflow.dto.TaskDto;
import com.yil.workflow.exception.*;
import com.yil.workflow.model.Action;
import com.yil.workflow.model.Flow;
import com.yil.workflow.model.Task;
import com.yil.workflow.service.ActionService;
import com.yil.workflow.service.FlowService;
import com.yil.workflow.service.TaskActionService;
import com.yil.workflow.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/wf/v1/home")
public class HomeController {

    private final TaskActionService taskActionService;
    private final FlowService flowService;
    private final ActionService actionService;
    private final TaskService taskService;
    private final Mapper<Flow, FlowDto> mapper = new Mapper<>(FlowService::convert);
    private final Mapper<Action, ActionDto> actionMapper = new Mapper<>(ActionService::convert);
    private final Mapper<Task, TaskDto> taskMapper = new Mapper<>(TaskService::convert);


    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "/{taskId}/actions")
    public ResponseEntity<List<ActionDto>> getNextActions(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                          @PathVariable Long taskId) throws ActionNotFoundException, StepNotFoundException, TaskNotFoundException, TaskActionNotFoundException {
        List<ActionDto> actions = actionMapper.map(taskActionService.getNextActions(taskId, authenticatedUserId));
        return ResponseEntity.ok(actions);
    }

    @GetMapping(value = "/flows")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<FlowDto>> getStartUpFlows(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId) {
        List<FlowDto> dto = mapper.map(flowService.getStartUpFlows(authenticatedUserId));
        return ResponseEntity.ok(dto);
    }


    @GetMapping(value = "/flows/{flowId}/actions")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ActionDto>> getStartActions(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                           @PathVariable Long flowId) throws FlowNotFoundException {
        if (!flowService.existsByIdAndEnabledTrueAndDeletedTimeIsNull(flowId))
            throw new FlowNotFoundException();
        List<ActionDto> actionDtos = actionMapper.map(actionService.getStartUpActions(flowId, authenticatedUserId));
        return ResponseEntity.ok(actionDtos);
    }

    @GetMapping(value = "/task")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TaskDto>> getMyTasks(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId) {
        List<TaskDto> dtos = taskMapper.map(taskService.getMyTasks(authenticatedUserId));
        return ResponseEntity.ok(dtos);
    }
}
