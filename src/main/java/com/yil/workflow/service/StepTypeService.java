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

@RequiredArgsConstructor
@Transactional
@Service
public class StepTypeService {
    private final StepTypeDao stepTypeDao;

    public static StepTypeDto toDto(StepType stepType) {
        if (stepType == null)
            throw new NullPointerException("Step Type is null");
        StepTypeDto dto = new StepTypeDto();
        dto.setId(stepType.getId());
        dto.setName(stepType.getName());
        dto.setDescription(stepType.getDescription());
        return dto;
    }

    public StepType findById(Integer id) throws StepTypeNotFoundException {
        return stepTypeDao.findById(id).orElseThrow(() -> new StepTypeNotFoundException());
    }

    public boolean existsById(Integer stepTypeId) {
        return stepTypeDao.existsById(stepTypeId);
    }

    public List<StepType> findAll() {
        return stepTypeDao.findAll();
    }
}
