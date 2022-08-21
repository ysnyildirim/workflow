package com.yil.workflow.service;

import com.yil.workflow.dto.TaskActionDocumentDto;
import com.yil.workflow.dto.TaskActionDocumentRequest;
import com.yil.workflow.dto.TaskActionDocumentResponse;
import com.yil.workflow.exception.TaskActionDocumentNotFoundException;
import com.yil.workflow.exception.YouDoNotHavePermissionException;
import com.yil.workflow.model.TaskActionDocument;
import com.yil.workflow.repository.TaskActionDocumentDao;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TaskActionDocumentService {

    private final TaskActionDocumentDao taskActionDocumentDao;

    public static TaskActionDocumentDto convert(TaskActionDocument taskActionDocument) {
        TaskActionDocumentDto dto = new TaskActionDocumentDto();
        dto.setId(taskActionDocument.getId());
        dto.setTaskActionId(taskActionDocument.getTaskActionId());
        dto.setExtension(taskActionDocument.getExtension());
        dto.setName(taskActionDocument.getName());
        return dto;
    }

    @Transactional(readOnly = true)
    public Page<TaskActionDocument> findAllByTaskActionIdAndDeletedTimeIsNull(Pageable pageable, Long taskActionId) {
        return taskActionDocumentDao.findAllByTaskActionId(pageable, taskActionId);
    }

    @Transactional(readOnly = true)
    public TaskActionDocument findByIdAndTaskActionIdAndDeletedTimeIsNull(Long id, Long taskActionId) throws TaskActionDocumentNotFoundException {
        return taskActionDocumentDao.findByIdAndTaskActionId(id, taskActionId).orElseThrow(TaskActionDocumentNotFoundException::new);

    }

    @Transactional(readOnly = true)
    public TaskActionDocument findById(long id) throws TaskActionDocumentNotFoundException {
        return taskActionDocumentDao.findById(id).orElseThrow(TaskActionDocumentNotFoundException::new);
    }

    @Transactional(rollbackFor = {Throwable.class})
    public TaskActionDocumentResponse save(TaskActionDocumentRequest doc, long taskActionId, long userId) {
        TaskActionDocument taskActionDocument = new TaskActionDocument();
        taskActionDocument.setTaskActionId(taskActionId);
        taskActionDocument.setName(doc.getName());
        taskActionDocument.setExtension(doc.getExtension());
        taskActionDocument.setContent(doc.getContent());
        taskActionDocument = taskActionDocumentDao.save(taskActionDocument);

        return TaskActionDocumentResponse
                .builder()
                .id(taskActionDocument.getId())
                .build();
    }

    @Transactional(rollbackFor = {Throwable.class})
    public void delete(long id, long userId) throws YouDoNotHavePermissionException {
        if (!isDeletabled(id, userId))
            throw new YouDoNotHavePermissionException();
        taskActionDocumentDao.deleteById(id);
    }

    public boolean isDeletabled(long id, long userId) {
        return true;
    }
}
