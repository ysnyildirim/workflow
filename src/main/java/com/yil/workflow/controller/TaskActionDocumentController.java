package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.Mapper;
import com.yil.workflow.base.PageDto;
import com.yil.workflow.dto.TaskActionDocumentDto;
import com.yil.workflow.dto.TaskActionDocumentRequest;
import com.yil.workflow.dto.TaskActionDocumentResponse;
import com.yil.workflow.exception.TaskActionDocumentNotFoundException;
import com.yil.workflow.exception.TaskActionNotFoundException;
import com.yil.workflow.model.TaskActionDocument;
import com.yil.workflow.service.TaskActionDocumentService;
import com.yil.workflow.service.TaskActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/wf/v1/task-actions/{taskActionId}/documents")
public class TaskActionDocumentController {

    private final TaskActionDocumentService taskActionDocumentService;
    private final TaskActionService taskActionService;
    private final Mapper<TaskActionDocument, TaskActionDocumentDto> mapper = new Mapper<>(TaskActionDocumentService::convert);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PageDto<TaskActionDocumentDto>> findAll(
            @PathVariable Long taskActionId,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        Pageable pageable = PageRequest.of(page, size);
        PageDto<TaskActionDocumentDto> pageDto = mapper.map(taskActionDocumentService.findAllByTaskActionId(pageable, taskActionId));
        return ResponseEntity.ok(pageDto);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TaskActionDocumentDto> findByIdAndTaskActionId(
            @PathVariable Long taskActionId,
            @PathVariable Long id) throws TaskActionDocumentNotFoundException {
        return ResponseEntity.ok(mapper.map(taskActionDocumentService.findByIdAndTaskActionId(id, taskActionId)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TaskActionDocumentResponse> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                             @PathVariable Long taskActionId,
                                                             @Valid @RequestBody TaskActionDocumentRequest request) throws TaskActionNotFoundException {
        if (!taskActionService.existsById(taskActionId))
            throw new TaskActionNotFoundException();
        TaskActionDocumentResponse responce = taskActionDocumentService.save(request, taskActionId, authenticatedUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responce);
    }


}
