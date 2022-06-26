package com.yil.workflow.service;

import com.yil.workflow.dto.TaskActionDocumentDto;
import com.yil.workflow.dto.TaskActionDocumentRequest;
import com.yil.workflow.dto.TaskActionDocumentResponse;
import com.yil.workflow.exception.TaskActionDocumentNotFoundException;
import com.yil.workflow.model.Document;
import com.yil.workflow.model.TaskActionDocument;
import com.yil.workflow.repository.TaskActionDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class TaskActionDocumentService {

    private final DocumentService documentService;
    private final TaskActionDocumentRepository taskActionDocumentRepository;

    public static TaskActionDocumentDto toDto(TaskActionDocument taskActionDocument) throws NullPointerException {
        if (taskActionDocument == null)
            throw new NullPointerException("TaskActionDocument is null");
        TaskActionDocumentDto dto = new TaskActionDocumentDto();
        dto.setId(taskActionDocument.getId());
        dto.setTaskActionId(taskActionDocument.getTaskActionId());
        dto.setDocumentId(taskActionDocument.getDocumentId());
        dto.setExtension(taskActionDocument.getExtension());
        dto.setName(taskActionDocument.getName());
        dto.setUploadedDate(taskActionDocument.getUploadedDate());
        return dto;
    }

    public Page<TaskActionDocument> findAllByTaskActionIdAndDeletedTimeIsNull(Pageable pageable, Long taskActionId) {
        return taskActionDocumentRepository.findAllByTaskActionIdAndDeletedTimeIsNull(pageable, taskActionId);
    }

    public TaskActionDocument findByIdAndTaskActionIdAndDeletedTimeIsNull(Long id, Long taskActionId) throws TaskActionDocumentNotFoundException {
        return taskActionDocumentRepository.findByIdAndTaskActionIdAndDeletedTimeIsNull(id, taskActionId).orElseThrow(() -> new TaskActionDocumentNotFoundException());

    }

    public TaskActionDocument findByIdAndDeletedTimeIsNull(long id) throws TaskActionDocumentNotFoundException {
        return taskActionDocumentRepository.findByIdAndDeletedTimeIsNull(id).orElseThrow(() -> new TaskActionDocumentNotFoundException());
    }

    @Transactional
    public TaskActionDocumentResponse save(TaskActionDocumentRequest doc, long taskActionId, long userId) {
        Document document = new Document();
        document.setContent(doc.getContent());
        document = documentService.save(document);

        TaskActionDocument taskActionDocument = new TaskActionDocument();
        taskActionDocument.setTaskActionId(taskActionId);
        taskActionDocument.setName(doc.getName());
        taskActionDocument.setExtension(doc.getExtension());
        taskActionDocument.setUploadedDate(doc.getUploadedDate());
        taskActionDocument.setDocumentId(document.getId());
        taskActionDocument.setCreatedUserId(userId);
        taskActionDocument.setCreatedTime(new Date());
        taskActionDocument = taskActionDocumentRepository.save(taskActionDocument);

        return TaskActionDocumentResponse
                .builder()
                .documentId(taskActionDocument.getDocumentId())
                .id(taskActionDocument.getId())
                .taskActionId(taskActionDocument.getTaskActionId())
                .build();
    }

    @Transactional
    public void delete(long id, long authenticatedUserId) throws TaskActionDocumentNotFoundException {
        TaskActionDocument entity = findByIdAndDeletedTimeIsNull(id);
        entity.setDeletedUserId(authenticatedUserId);
        entity.setDeletedTime(new Date());
        entity = taskActionDocumentRepository.save(entity);
    }
}
