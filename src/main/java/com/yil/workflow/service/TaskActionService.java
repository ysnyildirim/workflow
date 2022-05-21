package com.yil.workflow.service;

import com.yil.workflow.dto.TaskActionDto;
import com.yil.workflow.model.TaskAction;
import com.yil.workflow.repository.TaskActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class TaskActionService {

    private final TaskActionRepository taskActionRepository;

    @Autowired
    public TaskActionService(TaskActionRepository taskActionRepository) {
        this.taskActionRepository = taskActionRepository;
    }

    public static TaskActionDto toDto(TaskAction taskAction) throws NullPointerException {
        if (taskAction == null)
            throw new NullPointerException("TaskAction is null");
        TaskActionDto dto = new TaskActionDto();
        dto.setId(taskAction.getId());
        dto.setActionId(taskAction.getActionId());
        dto.setTaskId(taskAction.getTaskId());
        dto.setUserId(taskAction.getUserId());
        return dto;
    }

    public TaskAction findById(Long id) throws EntityNotFoundException {
        return taskActionRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException();
        });
    }

    public TaskAction save(TaskAction taskAction) {
        return taskActionRepository.save(taskAction);
    }

    public Page<TaskAction> findAllByDeletedTimeIsNull(Pageable pageable) {
        return taskActionRepository.findAllByDeletedTimeIsNull(pageable);
    }

}
