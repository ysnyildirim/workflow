package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.Mapper;
import com.yil.workflow.dto.ActionPermissionDto;
import com.yil.workflow.dto.ActionPermissionRequest;
import com.yil.workflow.dto.ActionPermissionResponse;
import com.yil.workflow.exception.ActionNotFoundException;
import com.yil.workflow.exception.ActionPermissionNotFoundException;
import com.yil.workflow.exception.ActionPermissionTypeNotFoundException;
import com.yil.workflow.model.ActionPermission;
import com.yil.workflow.service.ActionPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/wf/v1/action-permissions")
public class ActionPermissionController {
    private final ActionPermissionService actionPermissionService;
    private final Mapper<ActionPermission, ActionPermissionDto> mapper = new Mapper<>(ActionPermissionService::toDto);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ActionPermissionDto>> findAllByActionId(@RequestParam Long actionId) {
        return ResponseEntity.ok(mapper.map(actionPermissionService.findAllByActionId(actionId)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ActionPermissionResponse> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                           @RequestParam Long actionId,
                                                           @Valid @RequestBody ActionPermissionRequest request) throws ActionNotFoundException, ActionPermissionTypeNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(actionPermissionService.save(request, actionId));
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        actionPermissionService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> replace(@PathVariable Integer id,
                                          @Valid @RequestBody ActionPermissionRequest request) throws ActionPermissionNotFoundException {
        actionPermissionService.replace(request, id);
        return ResponseEntity.ok("Ok");
    }
}
