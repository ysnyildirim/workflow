package com.yil.workflow.service;

import com.yil.workflow.dto.StatusDto;
import com.yil.workflow.model.Status;
import com.yil.workflow.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class TaskStatusService {

    private final StatusRepository statusRepository;

    @Autowired
    public TaskStatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    public static StatusDto toDto(Status status) throws NullPointerException {
        if (status == null)
            throw new NullPointerException("TaskStatus is null");
        StatusDto dto = new StatusDto();
        dto.setId(status.getId());
        dto.setName(status.getName());
        dto.setIsClosed(status.getIsClosed());
        return dto;
    }

    public Status findById(Long id) throws EntityNotFoundException {
        return statusRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException();
        });
    }

    public Status save(Status status) {
        return statusRepository.save(status);
    }

    public Page<Status> findAllByDeletedTimeIsNull(Pageable pageable) {
        return statusRepository.findAllByDeletedTimeIsNull(pageable);
    }

    public boolean existsAllByNameAndDeletedTimeIsNull(String name) {
        return statusRepository.existsAllByNameAndDeletedTimeIsNull(name);
    }
}
