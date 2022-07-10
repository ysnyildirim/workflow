/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.service;

import com.yil.workflow.dto.StepTypeDto;
import com.yil.workflow.exception.StepTypeNotFoundException;
import com.yil.workflow.model.StepType;
import com.yil.workflow.repository.StepTypeDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * id(1).name("Start").des
 * id(2).name("Normal").de
 * id(3).name("Complete").
 * id(4).name("Denied").de
 * id(5).name("Cancelled")
 */
@RequiredArgsConstructor
@Service
public class StepTypeService {
    private final StepTypeDao stepTypeDao;

    public static StepTypeDto convert(StepType stepType) {
        StepTypeDto dto = new StepTypeDto();
        dto.setId(stepType.getId());
        dto.setName(stepType.getName());
        dto.setDescription(stepType.getDescription());
        return dto;
    }

    @Transactional(readOnly = true)
    public StepType findById(Integer id) throws StepTypeNotFoundException {
        return stepTypeDao.findById(id).orElseThrow(StepTypeNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public boolean existsById(Integer stepTypeId) {
        return stepTypeDao.existsById(stepTypeId);
    }

    @Transactional(readOnly = true)
    public List<StepType> findAll() {
        return stepTypeDao.findAll();
    }
}
