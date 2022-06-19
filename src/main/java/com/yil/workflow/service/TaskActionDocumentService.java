package com.yil.workflow.service;

import com.yil.workflow.dto.TaskActionDocumentDto;
import com.yil.workflow.exception.TaskActionDocumentNotFoundException;
import com.yil.workflow.model.TaskActionDocument;
import com.yil.workflow.repository.TaskActionDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TaskActionDocumentService {

    private final TaskActionDocumentRepository taskActionDocumentRepository;

    @Autowired
    public TaskActionDocumentService(TaskActionDocumentRepository taskActionDocumentRepository) {
        this.taskActionDocumentRepository = taskActionDocumentRepository;
    }

    public static TaskActionDocumentDto toDto(TaskActionDocument taskActionDocument) throws NullPointerException {
        if (taskActionDocument == null)
            throw new NullPointerException("TaskActionDocument is null");
        TaskActionDocumentDto dto = new TaskActionDocumentDto();
        dto.setId(taskActionDocument.getId());
        dto.setTaskActionId(taskActionDocument.getTaskActionId());
        dto.setExtension(taskActionDocument.getExtension());
        dto.setName(taskActionDocument.getName());
        dto.setUploadedDate(taskActionDocument.getUploadedDate());
        return dto;
    }

    public TaskActionDocument save(TaskActionDocument taskActionDocument) {
        return taskActionDocumentRepository.save(taskActionDocument);
    }

    public Page<TaskActionDocument> findAllByDeletedTimeIsNull(Pageable pageable) {
        return taskActionDocumentRepository.findAllByDeletedTimeIsNull(pageable);
    }

    public Page<TaskActionDocument> findAllByTaskActionIdAndDeletedTimeIsNull(Pageable pageable, Long taskActionId) {
        return taskActionDocumentRepository.findAllByTaskActionIdAndDeletedTimeIsNull(pageable, taskActionId);
    }

    public TaskActionDocument findByIdAndTaskActionIdAndDeletedTimeIsNull(Long id, Long taskActionId) throws TaskActionDocumentNotFoundException {
        return taskActionDocumentRepository.findByIdAndTaskActionIdAndDeletedTimeIsNull(id, taskActionId).orElseThrow(() -> new TaskActionDocumentNotFoundException());
    }
}
