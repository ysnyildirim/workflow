package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.PageDto;
import com.yil.workflow.dto.ActionDto;
import com.yil.workflow.dto.CreateActionDto;
import com.yil.workflow.exception.ActionNotFoundException;
import com.yil.workflow.model.Action;
import com.yil.workflow.service.ActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/wf/v1/steps/{stepId}/actions")
public class ActionController {

    private final ActionService actionService;

    @GetMapping
    public ResponseEntity<PageDto<ActionDto>> findAll(@PathVariable Long stepId,
                                                      @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
                                                      @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        Pageable pageable = PageRequest.of(page, size);
        Page<Action> actionPage = actionService.findAllByStepIdAndDeletedTimeIsNull(pageable, stepId);
        PageDto<ActionDto> pageDto = PageDto.toDto(actionPage, ActionService::toDto);
        return ResponseEntity.ok(pageDto);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<ActionDto> findByIdAndStepId(@PathVariable Long stepId,
                                                       @PathVariable Long id) throws ActionNotFoundException {
        Action action = actionService.findByIdAndStepId(id, stepId);
        ActionDto dto = ActionService.toDto(action);
        return ResponseEntity.ok(dto);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ActionDto> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                            @PathVariable Long stepId,
                                            @Valid @RequestBody CreateActionDto request) {
        Action action = new Action();
        action.setStepId(stepId);
        action.setName(request.getName());
        action.setDescription(request.getDescription());
        action.setEnabled(request.getEnabled());
        action.setNextStepId(request.getNextStepId());
        action.setPermissionId(request.getPermissionId());
        action.setCreatedUserId(authenticatedUserId);
        action.setCreatedTime(new Date());
        action = actionService.save(action);
        ActionDto dto = ActionService.toDto(action);
        return ResponseEntity.created(null).body(dto);
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                  @PathVariable Long stepId,
                                  @PathVariable Long id,
                                  @Valid @RequestBody CreateActionDto request) throws ActionNotFoundException {
        Action action = actionService.findByIdAndStepIdAndDeletedTimeIsNull(id, stepId);
        action.setStepId(stepId);
        action.setName(request.getName());
        action.setDescription(request.getDescription());
        action.setEnabled(request.getEnabled());
        action.setNextStepId(request.getNextStepId());
        action.setPermissionId(request.getPermissionId());
        action = actionService.save(action);
        ActionDto dto = ActionService.toDto(action);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                         @PathVariable Long stepId,
                                         @PathVariable Long id) throws ActionNotFoundException {
        Action action = actionService.findByIdAndStepIdAndDeletedTimeIsNull(id, stepId);
        if (action.getStepId().equals(stepId))
            throw new ActionNotFoundException();
        action.setDeletedUserId(authenticatedUserId);
        action.setDeletedTime(new Date());
        actionService.save(action);
        return ResponseEntity.ok("Action deleted.");
    }


}
