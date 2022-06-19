package com.yil.workflow.service;

import com.yil.workflow.dto.StatusDto;
import com.yil.workflow.exception.StatusNotFoundException;
import com.yil.workflow.model.Status;
import com.yil.workflow.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class StatusService {

    private final StatusRepository statusRepository;

    @Autowired
    public StatusService(StatusRepository statusRepository) {
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

    public Status findByIdAndDeletedTimeIsNull(Long id) throws StatusNotFoundException {
        return statusRepository.findByIdAndDeletedTimeIsNull(id).orElseThrow(() -> new StatusNotFoundException());

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
