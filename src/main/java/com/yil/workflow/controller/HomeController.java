/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.Mapper;
import com.yil.workflow.base.PageDto;
import com.yil.workflow.dto.*;
import com.yil.workflow.exception.*;
import com.yil.workflow.model.Action;
import com.yil.workflow.model.Flow;
import com.yil.workflow.model.Task;
import com.yil.workflow.service.ActionService;
import com.yil.workflow.service.FlowService;
import com.yil.workflow.service.TaskActionService;
import com.yil.workflow.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Date;
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
        List<ActionDto> actions = taskActionService.getNextActions(taskId, authenticatedUserId);
        return ResponseEntity.ok(actions);
    }

    @GetMapping(value = "/flows")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<StartUpFlowResponce>> getStartUpFlows(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId) throws TargetNotFoundException {
        List<StartUpFlowResponce> dto = flowService.getStartUpFlows(authenticatedUserId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/task")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PageDto<TaskDto>> getMyTasks(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                       @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
                                                       @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        Pageable pageable = PageRequest.of(page, size);
        PageDto<TaskDto> dtos = taskMapper.map(taskService.getMyTask(pageable, authenticatedUserId));
        return ResponseEntity.ok(dtos);
    }

}
