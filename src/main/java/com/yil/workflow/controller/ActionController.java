package com.yil.workflow.controller;

import com.yil.workflow.base.ApiHeaders;
import com.yil.workflow.base.PageDto;
import com.yil.workflow.dto.ActionDto;
import com.yil.workflow.dto.CreateActionDto;
import com.yil.workflow.model.Action;
import com.yil.workflow.service.ActionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping(value = "v1/steps/{stepId}/actions")
public class ActionController {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final ActionService actionService;

    @Autowired
    public ActionController(ActionService actionService) {
        this.actionService = actionService;
    }

    @GetMapping
    public ResponseEntity<PageDto<ActionDto>> findAll(@PathVariable Long stepId,
                                                      @RequestParam(required = false, defaultValue = "0") int page,
                                                      @RequestParam(required = false, defaultValue = "1000") int size) {
        try {
            if (page < 0)
                page = 0;
            if (size <= 0 || size > 1000)
                size = 1000;
            Pageable pageable = PageRequest.of(page, size);
            Page<Action> actionPage = actionService.findAllByStepIdAndDeletedTimeIsNull(pageable,stepId);
            PageDto<ActionDto> pageDto = PageDto.toDto(actionPage, ActionService::toDto);
            return ResponseEntity.ok(pageDto);
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<ActionDto> findById(@PathVariable Long stepId,
                                              @PathVariable Long id) {
        try {
            Action action;
            try {
                action = actionService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            if (action.getStepId().equals(stepId))
                return ResponseEntity.notFound().build();
            ActionDto dto = ActionService.toDto(action);
            return ResponseEntity.ok(dto);
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long stepId,
                                 @Valid @RequestBody CreateActionDto dto) {
        try {
            Action action = new Action();
            action.setStepId(stepId);
            action.setName(dto.getName());
            action.setDescription(dto.getDescription());
            action.setEnabled(dto.getEnabled());
            action.setNextStepId(dto.getNextStepId());
            action.setPermissionId(dto.getPermissionId());
            action.setCreatedUserId(authenticatedUserId);
            action.setCreatedTime(new Date());
            action = actionService.save(action);
            return ResponseEntity.created(null).build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity replace(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                  @PathVariable Long stepId,
                                  @PathVariable Long id,
                                  @Valid @RequestBody CreateActionDto dto) {
        try {
            Action action = null;
            try {
                action = actionService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            if (action.getStepId().equals(stepId))
                return ResponseEntity.notFound().build();
            action.setStepId(stepId);
            action.setName(dto.getName());
            action.setDescription(dto.getDescription());
            action.setEnabled(dto.getEnabled());
            action.setNextStepId(dto.getNextStepId());
            action.setPermissionId(dto.getPermissionId());
            action = actionService.save(action);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                         @PathVariable Long stepId,
                                         @PathVariable Long id) {
        try {
            Action action;
            try {
                action = actionService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            if (action.getStepId().equals(stepId))
                return ResponseEntity.notFound().build();
            action.setDeletedUserId(authenticatedUserId);
            action.setDeletedTime(new Date());
            actionService.save(action);
            return ResponseEntity.ok("Action deleted.");
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


}
