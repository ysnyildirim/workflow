package com.yil.workflow.service;

import com.yil.workflow.dto.PriorityTypeDto;
import com.yil.workflow.exception.PriorityNotFoundException;
import com.yil.workflow.model.PriorityType;
import com.yil.workflow.repository.PriorityTypeDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PriorityTypeService {

    private final PriorityTypeDao priorityTypeDao;

    public static PriorityTypeDto toDto(PriorityType priorityType) throws NullPointerException {
        if (priorityType == null)
            throw new NullPointerException("TaskPriority is null");
        PriorityTypeDto dto = new PriorityTypeDto();
        dto.setId(priorityType.getId());
        dto.setName(priorityType.getName());
        dto.setDescription(priorityType.getDescription());
        return dto;
    }

    @Transactional(readOnly = true)
    public PriorityType findById(Integer id) throws PriorityNotFoundException {
        return priorityTypeDao.findById(id).orElseThrow(() -> {
            return new PriorityNotFoundException();
        });
    }

    @Transactional(readOnly = true)
    public boolean existsByIdAndDeletedTimeIsNull(Integer id) {
        return priorityTypeDao.existsById(id);
    }

    @Transactional(readOnly = true)
    public List<PriorityType> findAll() {
        return priorityTypeDao.findAll();
    }
}
