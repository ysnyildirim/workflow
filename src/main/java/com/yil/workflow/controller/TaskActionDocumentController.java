package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.PageDto;
import com.yil.workflow.dto.CreateTaskActionDocumentDto;
import com.yil.workflow.dto.TaskActionDocumentDetailDto;
import com.yil.workflow.dto.TaskActionDocumentDto;
import com.yil.workflow.exception.DocumentNotFoundException;
import com.yil.workflow.exception.TaskActionDocumentNotFoundException;
import com.yil.workflow.model.Document;
import com.yil.workflow.model.TaskActionDocument;
import com.yil.workflow.service.DocumentService;
import com.yil.workflow.service.TaskActionDocumentService;
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
@RequestMapping(value = "/api/wf/v1/task-actions/{taskActionId}/documents")
public class TaskActionDocumentController {

    private final TaskActionDocumentService taskActionDocumentService;
    private final DocumentService documentService;

    @GetMapping
    public ResponseEntity<PageDto<TaskActionDocumentDto>> findAll(
            @PathVariable Long taskActionId,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        Pageable pageable = PageRequest.of(page, size);
        Page<TaskActionDocument> taskPage = taskActionDocumentService.findAllByTaskActionIdAndDeletedTimeIsNull(pageable, taskActionId);
        PageDto<TaskActionDocumentDto> pageDto = PageDto.toDto(taskPage, TaskActionDocumentService::toDto);
        return ResponseEntity.ok(pageDto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TaskActionDocumentDetailDto> findByIdAndTaskActionIdAAndDeletedTimeIsNull(
            @PathVariable Long taskActionId,
            @PathVariable Long id) throws TaskActionDocumentNotFoundException, DocumentNotFoundException {
        TaskActionDocument task = taskActionDocumentService.findByIdAndTaskActionIdAAndDeletedTimeIsNull(id, taskActionId);
        Document document = documentService.findByIdAndDeletedTimeIsNull(task.getDocumentId());
        TaskActionDocumentDetailDto dto = new TaskActionDocumentDetailDto();
        dto.setContent(document.getContent());
        dto.setTaskActionId(task.getTaskActionId());
        dto.setExtension(task.getExtension());
        dto.setName(task.getName());
        dto.setUploadedDate(task.getUploadedDate());
        dto.setId(task.getId());
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TaskActionDocumentDto> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                        @PathVariable Long taskActionId,
                                                        @Valid @RequestBody CreateTaskActionDocumentDto request) {
        Document document = new Document();
        document.setContent(request.getContent());
        document = documentService.save(document);
        TaskActionDocument entity = new TaskActionDocument();
        entity.setTaskActionId(taskActionId);
        entity.setDocumentId(document.getId());
        entity.setExtension(request.getExtension());
        entity.setName(request.getName());
        entity.setUploadedDate(request.getUploadedDate());
        entity.setCreatedUserId(authenticatedUserId);
        entity.setCreatedTime(new Date());
        entity = taskActionDocumentService.save(entity);
        TaskActionDocumentDto dto = TaskActionDocumentService.toDto(entity);
        return ResponseEntity.created(null).body(dto);
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TaskActionDocumentDto> replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                         @PathVariable Long taskActionId,
                                                         @PathVariable Long id,
                                                         @Valid @RequestBody CreateTaskActionDocumentDto request) throws TaskActionDocumentNotFoundException, DocumentNotFoundException {

        TaskActionDocument entity = taskActionDocumentService.findByIdAndTaskActionIdAAndDeletedTimeIsNull(id, taskActionId);
        Document document = documentService.findByIdAndDeletedTimeIsNull(id);
        document.setContent(request.getContent());
        documentService.save(document);
        entity.setExtension(request.getExtension());
        entity.setName(request.getName());
        entity = taskActionDocumentService.save(entity);
        TaskActionDocumentDto dto = TaskActionDocumentService.toDto(entity);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                         @PathVariable Long taskActionId,
                                         @PathVariable Long id) throws TaskActionDocumentNotFoundException {
        TaskActionDocument entity = taskActionDocumentService.findByIdAndTaskActionIdAAndDeletedTimeIsNull(id, taskActionId);
        entity.setDeletedUserId(authenticatedUserId);
        entity.setDeletedTime(new Date());
        entity = taskActionDocumentService.save(entity);
        return ResponseEntity.ok("Task action document deleted.");
    }


}
