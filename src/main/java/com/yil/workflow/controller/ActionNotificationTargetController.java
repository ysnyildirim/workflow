package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.Mapper;
import com.yil.workflow.dto.ActionNotificationTargetDto;
import com.yil.workflow.dto.ActionNotificationTargetRequest;
import com.yil.workflow.dto.ActionNotificationTargetResponse;
import com.yil.workflow.exception.ActionNotificationTargetNotFoundException;
import com.yil.workflow.model.ActionNotificationTarget;
import com.yil.workflow.service.ActionNotificationTargetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/wf/v1/action-notification-target")
public class ActionNotificationTargetController {
    private final ActionNotificationTargetService actionNotificationTargetService;
    private final Mapper<ActionNotificationTarget, ActionNotificationTargetDto> mapper = new Mapper<>(ActionNotificationTargetService::toDto);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ActionNotificationTargetDto>> findAllByActionId(@RequestParam Long actionNotificationId) {
        return ResponseEntity.ok(mapper.map(actionNotificationTargetService.findAllByActionNotificationId(actionNotificationId)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ActionNotificationTargetResponse> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                                   @RequestParam Long actionId,
                                                                   @Valid @RequestBody ActionNotificationTargetRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(actionNotificationTargetService.save(request, actionId));
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        actionNotificationTargetService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> replace(@PathVariable Long id,
                                          @Valid @RequestBody ActionNotificationTargetRequest request) throws ActionNotificationTargetNotFoundException {
        actionNotificationTargetService.replace(request, id);
        return ResponseEntity.ok("Ok");
    }
}
