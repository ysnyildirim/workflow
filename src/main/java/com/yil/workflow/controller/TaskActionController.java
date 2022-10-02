package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.Mapper;
import com.yil.workflow.base.PageDto;
import com.yil.workflow.dto.ActionDto;
import com.yil.workflow.dto.TaskActionDto;
import com.yil.workflow.dto.TaskActionRequest;
import com.yil.workflow.exception.*;
import com.yil.workflow.model.Action;
import com.yil.workflow.model.TaskAction;
import com.yil.workflow.service.ActionService;
import com.yil.workflow.service.TaskActionService;
import com.yil.workflow.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/wf/v1/tasks/{taskId}/actions")
public class TaskActionController {

    private final TaskActionService taskActionService;
    private final TaskService taskService;
    private final Mapper<TaskAction, TaskActionDto> mapper = new Mapper<>(TaskActionService::convert);
    private final Mapper<Action, ActionDto> actionMapper = new Mapper<>(ActionService::convert);

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "/{userId}/next")
    public ResponseEntity<ActionDto[]> getNextActions(@PathVariable Long userId,
                                                      @PathVariable Long taskId) throws ActionNotFoundException, StepNotFoundException, TaskNotFoundException {
        ActionDto[] actions = actionMapper.map(taskActionService.getNextActions(taskId, userId)).toArray(ActionDto[]::new);
        return ResponseEntity.ok(actions);
    }

    @GetMapping
    public ResponseEntity<PageDto<TaskActionDto>> findAll(
            @PathVariable Long taskId,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        Pageable pageable = PageRequest.of(page, size);
        PageDto<TaskActionDto> pageDto = mapper.map(taskActionService.findAllByTaskId(pageable, taskId));
        return ResponseEntity.ok(pageDto);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<TaskActionDto> findByIdAndTaskIdAndDeletedTimeIsNull(
            @PathVariable Long taskId,
            @PathVariable Long id) throws TaskActionNotFoundException {
        TaskActionDto dto = mapper.map(taskActionService.findByIdAndTaskId(id, taskId));
        return ResponseEntity.ok(dto);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TaskActionDto> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                @PathVariable Long taskId,
                                                @Valid @RequestBody TaskActionRequest request) throws ActionNotFoundException, YouDoNotHavePermissionException, TaskNotFoundException, StartUpActionException, NotNextActionException, StepNotFoundException, TaskActionNotFoundException {
        if (!taskService.existsById(taskId))
            throw new TaskNotFoundException();
        TaskActionDto responce = mapper.map(taskActionService.save(request, taskId, authenticatedUserId));
        return ResponseEntity.status(HttpStatus.CREATED).body(responce);
    }


}
