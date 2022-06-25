package com.yil.workflow.service;

import com.yil.workflow.dto.TaskActionMessageRequest;
import com.yil.workflow.dto.TaskActionMessageResponce;
import com.yil.workflow.dto.TaskActionMessageDto;
import com.yil.workflow.exception.TaskActionMessageNotFoundException;
import com.yil.workflow.model.TaskActionMessage;
import com.yil.workflow.repository.TaskActionMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@RequiredArgsConstructor
@Service
@Transactional
public class TaskActionMessageService {

    private final TaskActionMessageRepository taskActionMessageRepository;

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

    public TaskActionMessage findByIdAndTaskActionIdAndDeletedTimeIsNull(Long id, Long taskActionId) throws TaskActionMessageNotFoundException {
        return taskActionMessageRepository.findByIdAndTaskActionIdAndDeletedTimeIsNull(id, taskActionId).orElseThrow(() -> new TaskActionMessageNotFoundException());
    }

    public TaskActionMessage save(TaskActionMessage taskActionMessage) {
        return taskActionMessageRepository.save(taskActionMessage);
    }

    public Page<TaskActionMessage> findAllByTaskActionIdAndDeletedTimeIsNull(Pageable pageable, Long taskActionId) {
        return taskActionMessageRepository.findAllByTaskActionIdAndDeletedTimeIsNull(pageable, taskActionId);
    }

    public TaskActionMessageResponce save(TaskActionMessageRequest message, long taskActionId, long userId) {
        TaskActionMessage taskActionMessage = new TaskActionMessage();
        taskActionMessage.setTaskActionId(taskActionId);
        taskActionMessage.setSubject(message.getSubject());
        taskActionMessage.setContent(message.getContent());
        taskActionMessage.setCreatedUserId(userId);
        taskActionMessage.setCreatedTime(new Date());
        taskActionMessage = taskActionMessageRepository.save(taskActionMessage);
        return TaskActionMessageResponce
                .builder()
                .id(taskActionMessage.getId())
                .taskActionId(taskActionMessage.getTaskActionId())
                .build();
    }
}
