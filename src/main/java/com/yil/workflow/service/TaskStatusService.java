package com.yil.workflow.service;

import com.yil.workflow.dto.TaskStatusDto;
import com.yil.workflow.model.TaskStatus;
import com.yil.workflow.repository.TaskStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class TaskStatusService {

    private final TaskStatusRepository taskStatusRepository;

    @Autowired
    public TaskStatusService(TaskStatusRepository taskStatusRepository) {
        this.taskStatusRepository = taskStatusRepository;
    }

    public static TaskStatusDto toDto(TaskStatus taskStatus) throws NullPointerException {
        if (taskStatus == null)
            throw new NullPointerException("TaskStatus is null");
        TaskStatusDto dto = new TaskStatusDto();
        dto.setId(taskStatus.getId());
        dto.setName(taskStatus.getName());
        return dto;
    }

    public TaskStatus findById(Long id) throws EntityNotFoundException {
        return taskStatusRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException();
        });
    }

    public TaskStatus save(TaskStatus taskStatus) {
        return taskStatusRepository.save(taskStatus);
    }

    public Page<TaskStatus> findAllByDeletedTimeIsNull(Pageable pageable) {
        return taskStatusRepository.findAllByDeletedTimeIsNull(pageable);
    }

    public boolean existsAllByNameAndDeletedTimeIsNull(String name) {
        return taskStatusRepository.existsAllByNameAndDeletedTimeIsNull(name);
    }
}
