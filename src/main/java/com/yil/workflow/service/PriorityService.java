package com.yil.workflow.service;

import com.yil.workflow.dto.PriorityDto;
import com.yil.workflow.exception.PriorityNotFoundException;
import com.yil.workflow.model.Priority;
import com.yil.workflow.repository.PriorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class PriorityService {

    private final PriorityRepository priorityRepository;

    public static PriorityDto toDto(Priority priority) throws NullPointerException {
        if (priority == null)
            throw new NullPointerException("TaskPriority is null");
        PriorityDto dto = new PriorityDto();
        dto.setId(priority.getId());
        dto.setName(priority.getName());
        dto.setDescription(priority.getDescription());
        return dto;
    }

    public Priority findById(Integer id) throws PriorityNotFoundException {
        return priorityRepository.findById(id).orElseThrow(() -> {
            return new PriorityNotFoundException();
        });
    }

    public boolean existsByIdAndDeletedTimeIsNull(Integer id) {
        return priorityRepository.existsById(id);
    }

    public List<Priority> findAll() {
        return priorityRepository.findAll();
    }
}
