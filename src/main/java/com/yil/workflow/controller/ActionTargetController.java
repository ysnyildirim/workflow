package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.dto.ActionPermissionTypeDto;
import com.yil.workflow.dto.ActionTargetDtoRequest;
import com.yil.workflow.exception.ActionNotFoundException;
import com.yil.workflow.exception.ActionPermissionTypeNotFoundException;
import com.yil.workflow.model.ActionPermission;
import com.yil.workflow.model.ActionPermissionType;
import com.yil.workflow.service.ActionPermissionService;
import com.yil.workflow.service.ActionPermissionTypeService;
import com.yil.workflow.service.ActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/account/v1/action/{actionId}/targets")
public class ActionTargetController {

    private final ActionPermissionService actionPermissionService;
    private final ActionPermissionTypeService actionPermissionTypeService;
    private final ActionService actionService;

    @GetMapping
    public ResponseEntity<List<ActionPermissionTypeDto>> findAll(@PathVariable Long actionId) throws ActionPermissionTypeNotFoundException {
        List<ActionPermission> targets = actionPermissionService.findAllByActionId(actionId);
        List<ActionPermissionTypeDto> dtos = new ArrayList<>();
        for (ActionPermission target : targets) {
            ActionPermissionType targetType = actionPermissionTypeService.findById(target.getId().getActionPermissionTypeId());
            ActionPermissionTypeDto dto = ActionPermissionTypeService.convert(targetType);
            dtos.add(dto);
        }
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long actionId,
                                 @Valid @RequestBody ActionTargetDtoRequest dto) throws ActionNotFoundException, ActionPermissionTypeNotFoundException {

        if (!actionService.existsById(actionId))
            throw new ActionNotFoundException();
        if (!actionPermissionTypeService.existsById(dto.getTargetTypeId()))
            throw new ActionPermissionTypeNotFoundException();
        ActionPermission.Pk id = ActionPermission.Pk.builder().actionPermissionTypeId(dto.getTargetTypeId()).actionId(actionId).build();
        ActionPermission actionPermission = ActionPermission.builder().id(id).build();
        actionPermissionService.save(actionPermission);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long actionId,
                                 @PathVariable Integer id) {
        ActionPermission.Pk pk = ActionPermission.Pk.builder().actionId(actionId).actionPermissionTypeId(id).build();
        actionPermissionService.delete(pk);
        return ResponseEntity.ok().build();
    }

}
