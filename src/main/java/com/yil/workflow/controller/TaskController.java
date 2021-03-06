package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.Mapper;
import com.yil.workflow.base.PageDto;
import com.yil.workflow.dto.TaskBaseRequest;
import com.yil.workflow.dto.TaskDto;
import com.yil.workflow.dto.TaskRequest;
import com.yil.workflow.dto.TaskResponse;
import com.yil.workflow.exception.*;
import com.yil.workflow.model.Task;
import com.yil.workflow.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/wf/v1/tasks")
public class TaskController {

    private final TaskService taskService;
    private final Mapper<Task, TaskDto> mapper = new Mapper<>(TaskService::convert);

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping
    public ResponseEntity<PageDto<TaskDto>> findAll(
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        Pageable pageable = PageRequest.of(page, size);
        PageDto<TaskDto> pageDto = mapper.map(taskService.findAll(pageable));
        return ResponseEntity.ok(pageDto);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "/action-created={userId}")
    public ResponseEntity<PageDto<TaskDto>> findAllByActionCreatedUserId(
            @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        Pageable pageable = PageRequest.of(page, size);
        PageDto<TaskDto> pageDto = mapper.map(taskService.findAllByActionCreatedUserId(pageable, userId));
        return ResponseEntity.ok(pageDto);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "/assigned={userId}")
    public ResponseEntity<PageDto<TaskDto>> findAllByAssignedUserId(
            @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        Pageable pageable = PageRequest.of(page, size);
        PageDto<TaskDto> pageDto = mapper.map(taskService.findAllByAssignedUserId(pageable, userId));
        return ResponseEntity.ok(pageDto);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "/created={userId}")
    public ResponseEntity<PageDto<TaskDto>> findAllByCreatedUserId(
            @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        Pageable pageable = PageRequest.of(page, size);
        PageDto<TaskDto> pageDto = mapper.map(taskService.findAllByCreatedUserId(pageable, userId));
        return ResponseEntity.ok(pageDto);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "/{id}")
    public ResponseEntity<TaskDto> findByIdAndDeletedTimeIsNull(@PathVariable Long id) throws TaskNotFoundException {
        TaskDto dto = mapper.map(taskService.findById(id));
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TaskResponse> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                               @Valid @RequestBody TaskRequest request) throws ActionNotFoundException, PriorityNotFoundException, YouDoNotHavePermissionException, StepNotFoundException, NotNextActionException, StartUpActionException {
        TaskResponse responce = taskService.save(request, authenticatedUserId);
        return ResponseEntity.created(null).body(responce);
    }

    @Operation(summary = "Task bilgilerini de??i??tirmek i??in kullan??l??r. " +
            "Bilgileri sadece son aksiyondaki grup y??neticisi veya grup admini de??i??tirebilir.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Task bilgilerini g??ncelendi",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskResponse.class))}),
            @ApiResponse(responseCode = "403",
                    description = "Yetkiniz yok.",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Task bulunamad??,",
                    content = @Content)
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TaskResponse> replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                @PathVariable Long id,
                                                @Valid @RequestBody TaskBaseRequest request) throws TaskNotFoundException, TaskActionNotFoundException, PriorityNotFoundException {
        TaskResponse responce = taskService.replace(request, id, authenticatedUserId);
        return ResponseEntity.ok(responce);
    }


}
