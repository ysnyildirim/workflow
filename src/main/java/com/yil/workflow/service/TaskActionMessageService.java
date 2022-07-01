package com.yil.workflow.service;

import com.yil.workflow.dto.TaskActionMessageDto;
import com.yil.workflow.dto.TaskActionMessageRequest;
import com.yil.workflow.dto.TaskActionMessageResponse;
import com.yil.workflow.exception.TaskActionMessageNotFoundException;
import com.yil.workflow.exception.YouDoNotHavePermissionException;
import com.yil.workflow.model.TaskActionMessage;
import com.yil.workflow.repository.TaskActionMessageDao;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TaskActionMessageService {

    private final TaskActionMessageDao taskActionMessageDao;

    public static TaskActionMessageDto toDto(TaskActionMessage taskActionMessage) throws NullPointerException {
        if (taskActionMessage == null)
            throw new NullPointerException("TaskActionMessage is null");
        TaskActionMessageDto dto = new TaskActionMessageDto();
        dto.setId(taskActionMessage.getId());
        dto.setTaskActionId(taskActionMessage.getTaskActionId());
        dto.setContent(taskActionMessage.getContent());
        dto.setSubject(taskActionMessage.getSubject());
        return dto;
    }

    @Transactional(readOnly = true)
    public TaskActionMessage findByIdAndTaskActionId(Long id, Long taskActionId) throws TaskActionMessageNotFoundException {
        return taskActionMessageDao.findByIdAndTaskActionId(id, taskActionId).orElseThrow(() -> new TaskActionMessageNotFoundException());
    }

    @Transactional(readOnly = true)
    public Page<TaskActionMessage> findAllByTaskActionId(Pageable pageable, Long taskActionId) {
        return taskActionMessageDao.findAllByTaskActionId(pageable, taskActionId);
    }

    @Transactional(rollbackFor = {Throwable.class})
    public TaskActionMessageResponse save(TaskActionMessageRequest message, long taskActionId, long userId) {
        TaskActionMessage taskActionMessage = new TaskActionMessage();
        taskActionMessage.setTaskActionId(taskActionId);
        taskActionMessage.setSubject(message.getSubject());
        taskActionMessage.setContent(message.getContent());
        taskActionMessage = taskActionMessageDao.save(taskActionMessage);
        return TaskActionMessageResponse
                .builder()
                .id(taskActionMessage.getId())
                .taskActionId(taskActionMessage.getTaskActionId())
                .build();
    }

    @Transactional(rollbackFor = {Throwable.class})
    public void delete(long id, long userId) throws YouDoNotHavePermissionException {
        if (!isDeletabled(id, userId))
            throw new YouDoNotHavePermissionException();
        taskActionMessageDao.deleteById(id);
    }

    public boolean isDeletabled(long id, long userId) {
        return true;
    }
}
