package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.Mapper;
import com.yil.workflow.dto.ActionNotificationDto;
import com.yil.workflow.dto.ActionNotificationRequest;
import com.yil.workflow.dto.ActionNotificationResponse;
import com.yil.workflow.exception.ActionNotificationNotFoundException;
import com.yil.workflow.exception.FlowNotFoundException;
import com.yil.workflow.exception.StatusNotFoundException;
import com.yil.workflow.exception.StepTypeNotFoundException;
import com.yil.workflow.model.ActionNotification;
import com.yil.workflow.service.ActionNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/wf/v1/action-notification")
public class ActionNotificationController {
    private final ActionNotificationService actionNotificationService;
    private final Mapper<ActionNotification, ActionNotificationDto> mapper = new Mapper<>(ActionNotificationService::toDto);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ActionNotificationDto>> findAllByActionId(@RequestParam Long actionId) {
        return ResponseEntity.ok(mapper.map(actionNotificationService.findAllByActionId(actionId)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ActionNotificationResponse> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                             @RequestParam Long actionId,
                                                             @Valid @RequestBody ActionNotificationRequest request) throws FlowNotFoundException, StatusNotFoundException, StepTypeNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(actionNotificationService.save(request, actionId));
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        actionNotificationService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> replace(@PathVariable Long id,
                                          @Valid @RequestBody ActionNotificationRequest request) throws ActionNotificationNotFoundException {
        actionNotificationService.replace(request, id);
        return ResponseEntity.ok("Ok");
    }
}
