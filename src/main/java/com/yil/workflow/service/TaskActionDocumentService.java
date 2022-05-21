package com.yil.workflow.service;

import com.yil.workflow.dto.TaskActionDocumentDto;
import com.yil.workflow.model.TaskActionDocument;
import com.yil.workflow.repository.TaskActionDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

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
        dto.setDocumentId(taskActionDocument.getDocumentId());
        return dto;
    }

    public TaskActionDocument findById(Long id) throws EntityNotFoundException {
        return taskActionDocumentRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException();
        });
    }

    public TaskActionDocument save(TaskActionDocument taskActionDocument) {
        return taskActionDocumentRepository.save(taskActionDocument);
    }

    public Page<TaskActionDocument> findAllByDeletedTimeIsNull(Pageable pageable) {
        return taskActionDocumentRepository.findAllByDeletedTimeIsNull(pageable);
    }

}
