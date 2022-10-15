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
@Transactional(readOnly = true)
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
    public StepType findById(Integer id) throws StepTypeNotFoundException {
        return stepTypeDao.findById(id).orElseThrow(StepTypeNotFoundException::new);
    }

    @Cacheable(value = "stepTypes_existsById", key = "#id")
    public boolean existsById(Integer id) {
        return stepTypeDao.existsById(id);
    }

    @Cacheable(value = "stepTypes_existsById")
    public List<StepType> findAll() {
        return stepTypeDao.findAll();
    }
}
