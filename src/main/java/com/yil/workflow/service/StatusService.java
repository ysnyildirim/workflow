package com.yil.workflow.service;

import com.yil.workflow.dto.FlowRequest;
import com.yil.workflow.dto.StatusDto;
import com.yil.workflow.dto.StatusRequest;
import com.yil.workflow.exception.StatusNotFoundException;
import com.yil.workflow.model.Status;
import com.yil.workflow.repository.StatusDao;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class StatusService {
    private final StatusDao statusDao;

    public static StatusDto convert(Status status) {
        StatusDto dto = new StatusDto();
        dto.setId(status.getId());
        dto.setName(status.getName());
        dto.setDescription(status.getDescription());
        return dto;
    }

    @Cacheable(value = "status", key = "#pageable")
    @Transactional(readOnly = true)
    public Page<Status> findAll(Pageable pageable) {
        return statusDao.findAll(pageable);
    }

    @Cacheable(value = "status_existsById", key = "#id")
    @Transactional(readOnly = true)
    public boolean existsById(int id) {
        return statusDao.existsById(id);
    }

    @Transactional
    @CacheEvict(value = "status", allEntries = true)
    public Status save(StatusRequest request, Long authenticatedUserId) {
        Status status = new Status();
        status.setName(request.getName());
        status.setDescription(request.getDescription());
        return statusDao.save(status);
    }

    @Transactional
    @CacheEvict(value = "status", allEntries = true)
    public Status replace(FlowRequest request, Integer id, Long authenticatedUserId) throws StatusNotFoundException {
        Status status = findById(id);
        status.setName(request.getName());
        status.setDescription(request.getDescription());
        status = statusDao.save(status);
        return status;
    }

    @Cacheable(value = "status", key = "#id")
    @Transactional(readOnly = true)
    public Status findById(Integer id) throws StatusNotFoundException {
        return statusDao.findById(id).orElseThrow(StatusNotFoundException::new);
    }

    @CacheEvict(value = "status", allEntries = true)
    @Transactional
    public void delete(Integer id, Long authenticatedUserId) {
        statusDao.deleteById(id);
    }
}
