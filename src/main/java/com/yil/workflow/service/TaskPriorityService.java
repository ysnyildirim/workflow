package com.yil.workflow.service;

import com.yil.workflow.dto.PriorityDto;
import com.yil.workflow.model.Priority;
import com.yil.workflow.repository.PriorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class TaskPriorityService {

    private final PriorityRepository priorityRepository;

    @Autowired
    public TaskPriorityService(PriorityRepository priorityRepository) {
        this.priorityRepository = priorityRepository;
    }

    public static PriorityDto toDto(Priority priority) throws NullPointerException {
        if (priority == null)
            throw new NullPointerException("TaskPriority is null");
        PriorityDto dto = new PriorityDto();
        dto.setId(priority.getId());
        dto.setName(priority.getName());
        dto.setDescription(priority.getDescription());
        return dto;
    }

    public Priority findById(Long id) throws EntityNotFoundException {
        return priorityRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException();
        });
    }

    public Priority save(Priority priority) {
        return priorityRepository.save(priority);
    }

    public Page<Priority> findAllByDeletedTimeIsNull(Pageable pageable) {
        return priorityRepository.findAllByDeletedTimeIsNull(pageable);
    }

    public boolean existsAllByNameAndDeletedTimeIsNull(String name) {
        return priorityRepository.existsAllByNameAndDeletedTimeIsNull(name);
    }
}
