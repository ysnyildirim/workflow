package com.yil.workflow.service;

import com.yil.workflow.dto.StatusDto;
import com.yil.workflow.exception.StatusNotFoundException;
import com.yil.workflow.model.Status;
import com.yil.workflow.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class StatusService {

    private final StatusRepository statusRepository;

    public static StatusDto convert(Status status) {
        StatusDto dto = new StatusDto();
        dto.setId(status.getId());
        dto.setName(status.getName());
        return dto;
    }

    @Transactional(readOnly = true)
    public Status findById(Integer id) throws StatusNotFoundException {
        return statusRepository.findById(id).orElseThrow(StatusNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<Status> findAll(Pageable pageable) {
        return statusRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public boolean existsById(int statusId) {
        return statusRepository.existsById(statusId);
    }
}
