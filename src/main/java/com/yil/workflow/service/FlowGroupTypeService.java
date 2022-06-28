/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.service;

import com.yil.workflow.dto.FlowGroupTypeDto;
import com.yil.workflow.model.FlowGroupType;
import com.yil.workflow.repository.FlowGroupTypeDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FlowGroupTypeService {
    private final FlowGroupTypeDao flowGroupTypeDao;

    public static FlowGroupTypeDto toDto(FlowGroupType flowGroupType) {
        if (flowGroupType == null)
            throw new NullPointerException("Flow group type is null");
        FlowGroupTypeDto dto = new FlowGroupTypeDto();
        dto.setId(flowGroupType.getId());
        dto.setName(flowGroupType.getName());
        dto.setDescription(flowGroupType.getDescription());
        return dto;
    }

    public boolean existsById(Integer flowGroupTypeId) {
        return flowGroupTypeDao.existsById(flowGroupTypeId);
    }

    public List<FlowGroupTypeDto> findAll() {
        List<FlowGroupType> types = flowGroupTypeDao.findAll();
        List<FlowGroupTypeDto> dtos = new ArrayList<>();
        types.forEach(f -> {
            dtos.add(toDto(f));
        });
        return dtos;
    }
}
