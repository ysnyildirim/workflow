package com.yil.workflow.service;

import com.yil.workflow.dto.PriorityTypeDto;
import com.yil.workflow.exception.PriorityNotFoundException;
import com.yil.workflow.model.PriorityType;
import com.yil.workflow.repository.PriorityTypeDao;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PriorityTypeService {
    public static PriorityType Yuksek;
    public static PriorityType Orta;
    public static PriorityType Dusuk;
    public static PriorityType Kritik;
    private final PriorityTypeDao priorityTypeDao;

    public static PriorityTypeDto convert(PriorityType priorityType) {
        PriorityTypeDto dto = new PriorityTypeDto();
        dto.setId(priorityType.getId());
        dto.setName(priorityType.getName());
        dto.setDescription(priorityType.getDescription());
        return dto;
    }

    @Cacheable(value = "priorityTypes", key = "#id")
    public PriorityType findById(Integer id) throws PriorityNotFoundException {
        return priorityTypeDao.findById(id).orElseThrow(PriorityNotFoundException::new);
    }

    @Cacheable(value = "priorityTypes_existsById", key = "#id")
    public boolean existsById(Integer id) {
        return priorityTypeDao.existsById(id);
    }

    @Cacheable(value = "priorityTypes")
    public List<PriorityType> findAll() {
        return priorityTypeDao.findAll();
    }
}
