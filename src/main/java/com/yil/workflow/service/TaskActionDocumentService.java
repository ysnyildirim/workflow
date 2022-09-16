package com.yil.workflow.service;

import com.yil.workflow.dto.TaskActionDocumentDto;
import com.yil.workflow.dto.TaskActionDocumentRequest;
import com.yil.workflow.dto.TaskActionDocumentResponse;
import com.yil.workflow.exception.TaskActionDocumentNotFoundException;
import com.yil.workflow.exception.YouDoNotHavePermissionException;
import com.yil.workflow.model.Document;
import com.yil.workflow.model.TaskActionDocument;
import com.yil.workflow.repository.DocumentDao;
import com.yil.workflow.repository.TaskActionDocumentDao;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

@RequiredArgsConstructor
@Service
public class TaskActionDocumentService {

    private final TaskActionDocumentDao taskActionDocumentDao;
    private final DocumentDao documentDao;

    public static TaskActionDocumentDto convert(TaskActionDocument taskActionDocument) {
        TaskActionDocumentDto dto = new TaskActionDocumentDto();
        dto.setId(taskActionDocument.getId());
        dto.setTaskActionId(taskActionDocument.getTaskActionId());
        dto.setExtension(taskActionDocument.getExtension());
        dto.setName(taskActionDocument.getName());
        dto.setDocumentId(taskActionDocument.getDocumentId());
        return dto;
    }

    @Transactional(readOnly = true)
    public Page<TaskActionDocument> findAllByTaskActionId(Pageable pageable, Long taskActionId) {
        return taskActionDocumentDao.findAllByTaskActionId(pageable, taskActionId);
    }

    @Transactional(readOnly = true)
    public TaskActionDocument findByIdAndTaskActionId(Long id, Long taskActionId) throws TaskActionDocumentNotFoundException {
        return taskActionDocumentDao.findByIdAndTaskActionId(id, taskActionId).orElseThrow(TaskActionDocumentNotFoundException::new);

    }

    @Transactional(readOnly = true)
    public TaskActionDocument findById(long id) throws TaskActionDocumentNotFoundException {
        return taskActionDocumentDao.findById(id).orElseThrow(TaskActionDocumentNotFoundException::new);
    }

    @Transactional(rollbackFor = {Throwable.class})
    public TaskActionDocumentResponse save(TaskActionDocumentRequest doc, long taskActionId, long userId) {
        byte[] bytes = ArrayUtils.toPrimitive(doc.getContent());
        String hashValue = DigestUtils.md5DigestAsHex(bytes);

        if (!documentDao.existsById(hashValue)) {
            Document document = new Document();
            document.setContent(doc.getContent());
            document.setHashValue(hashValue);
            documentDao.save(document);
        }

        TaskActionDocument taskActionDocument = new TaskActionDocument();
        taskActionDocument.setDocumentId(hashValue);
        taskActionDocument.setTaskActionId(taskActionId);
        taskActionDocument.setName(doc.getName());
        taskActionDocument.setExtension(doc.getExtension());
        taskActionDocument = taskActionDocumentDao.save(taskActionDocument);

        TaskActionDocumentResponse response = new TaskActionDocumentResponse();
        response.setId(taskActionDocument.getId());
        return response;
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
