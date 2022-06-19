package com.yil.workflow.service;

import com.yil.workflow.dto.TaskActionMessageDto;
import com.yil.workflow.exception.TaskActionMessageNotFoundException;
import com.yil.workflow.model.TaskActionMessage;
import com.yil.workflow.repository.TaskActionMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class TaskActionMessageService {

    private final TaskActionMessageRepository taskActionMessageRepository;

    @Autowired
    public TaskActionMessageService(TaskActionMessageRepository taskActionMessageRepository) {
        this.taskActionMessageRepository = taskActionMessageRepository;
    }

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

    public TaskActionMessage findByIdAndTaskActionIdAndDeletedTimeIsNull(Long id,Long taskActionId) throws TaskActionMessageNotFoundException {
        return taskActionMessageRepository.findByIdAndTaskActionIdAndDeletedTimeIsNull(id,taskActionId).orElseThrow(() -> new TaskActionMessageNotFoundException());
    }

    public TaskActionMessage save(TaskActionMessage taskActionMessage) {
        return taskActionMessageRepository.save(taskActionMessage);
    }

    public Page<TaskActionMessage> findAllByDeletedTimeIsNull(Pageable pageable) {
        return taskActionMessageRepository.findAllByDeletedTimeIsNull(pageable);
    }

    public Page<TaskActionMessage> findAllByTaskActionIdAndDeletedTimeIsNull(Pageable pageable, Long taskActionId) {
        return taskActionMessageRepository.findAllByTaskActionIdAndDeletedTimeIsNull(pageable, taskActionId);
    }
}
