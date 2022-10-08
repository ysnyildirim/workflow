/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */
package com.yil.workflow.service;

import com.yil.workflow.dto.StepTypeDto;
import com.yil.workflow.exception.StepTypeNotFoundException;
import com.yil.workflow.model.StepType;
import com.yil.workflow.repository.StepTypeDao;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StepTypeService {
    public static StepType Start;
    public static StepType Normal;
    public static StepType Complete;
    private final StepTypeDao stepTypeDao;

    public static StepTypeDto convert(StepType stepType) {
        StepTypeDto dto = new StepTypeDto();
        dto.setId(stepType.getId());
        dto.setName(stepType.getName());
        dto.setDescription(stepType.getDescription());
        return dto;
    }

    @Cacheable(value = "stepTypes", key = "#id")
    @Transactional(readOnly = true)
    public StepType findById(Integer id) throws StepTypeNotFoundException {
        return stepTypeDao.findById(id).orElseThrow(StepTypeNotFoundException::new);
    }

    @Cacheable(value = "stepTypes_existsById", key = "#id")
    @Transactional(readOnly = true)
    public boolean existsById(Integer stepTypeId) {
        return stepTypeDao.existsById(stepTypeId);
    }

    @Cacheable(value = "stepTypes_existsById")
    @Transactional(readOnly = true)
    public List<StepType> findAll() {
        return stepTypeDao.findAll();
    }
}
