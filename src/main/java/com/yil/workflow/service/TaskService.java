package com.yil.workflow.service;

import com.yil.workflow.dto.TaskDto;
import com.yil.workflow.exception.TaskNotFoundException;
import com.yil.workflow.model.Task;
import com.yil.workflow.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public static TaskDto toDto(Task task) throws NullPointerException {
        if (task == null)
            throw new NullPointerException("Task is null");
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setFinishDate(task.getFinishDate());
        dto.setCurrentTaskActionId(task.getCurrentTaskActionId());
        dto.setEstimatedFinishDate(task.getEstimatedFinishDate());
        dto.setStatusId(task.getStatusId());
        dto.setPriorityId(task.getPriorityId());
        dto.setStartDate(task.getStartDate());
        dto.setFlowId(task.getFlowId());
        dto.setFlowId(task.getFlowId());
        return dto;
    }

    public Task findByIdAndDeletedTimeIsNull(Long id) throws TaskNotFoundException {
        return taskRepository.findByIdAndDeletedTimeIsNull(id).orElseThrow(() -> new TaskNotFoundException());
    }

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public Page<Task> findAllByDeletedTimeIsNull(Pageable pageable) {
        return taskRepository.findAllByDeletedTimeIsNull(pageable);
    }

}
